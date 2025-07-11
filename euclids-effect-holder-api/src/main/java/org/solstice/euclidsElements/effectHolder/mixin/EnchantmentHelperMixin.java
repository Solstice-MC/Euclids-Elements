package org.solstice.euclidsElements.effectHolder.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.effectHolder.api.EffectHolderHelper;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

	@WrapMethod(method = "forEachEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;)V")
	private static void wrapForEachEnchantment(ItemStack stack, EnchantmentHelper.Consumer consumer, Operation<Void> original) {
		try {
			EffectHolderHelper.forEachEffectHolder(stack, consumer);
		} catch (ClassCastException exception) {
			original.call(stack, consumer);
		}
	}

	@WrapMethod(method = "forEachEnchantment(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/enchantment/EnchantmentHelper$ContextAwareConsumer;)V")
	private static void wrapForEachEnchantment(LivingEntity entity, EnchantmentHelper.ContextAwareConsumer consumer, Operation<Void> original) {
		try {
			EffectHolderHelper.forEachEffectHolder(entity, consumer);
		} catch (ClassCastException exception) {
			original.call(entity, consumer);
		}
	}

	@WrapMethod(method = "forEachEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/enchantment/EnchantmentHelper$ContextAwareConsumer;)V")
	private static void wrapForEachEnchantment(ItemStack stack, EquipmentSlot slot, LivingEntity entity, EnchantmentHelper.ContextAwareConsumer consumer, Operation<Void> original) {
		try {
			EffectHolderHelper.forEachEffectHolder(stack, slot, entity, consumer);
		} catch (ClassCastException exception) {
			original.call(stack, slot, entity, consumer);
		}
	}

	@WrapMethod(method = "getEquipmentDropChance")
	private static float getEquipmentDropChance(ServerWorld world, LivingEntity target, DamageSource source, float base, Operation<Float> original) {
		return EffectHolderHelper.getEquipmentDropChance(world, target, source, base, original);
	}

	@WrapMethod(method = "chooseEquipmentWith")
	private static Optional<EnchantmentEffectContext> chooseEquipmentWith(ComponentType<?> componentType, LivingEntity entity, Predicate<ItemStack> stackPredicate, Operation<Optional<EnchantmentEffectContext>> original) {
		return EffectHolderHelper.chooseEquipmentWith(componentType, entity, stackPredicate, original);
	}

	@WrapMethod(method = "getItemDamage")
	private static int getItemDamage(ServerWorld world, ItemStack stack, int base, Operation<Integer> original) {
		MutableFloat result = new MutableFloat((float) base);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyItemDamage(world, level, stack, result)
		);
		return result.intValue();
	}

	@WrapMethod(method = "getAmmoUse")
	private static int getAmmoUse(ServerWorld world, ItemStack weaponStack, ItemStack projectileStack, int base, Operation<Integer> original) {
		MutableFloat result = new MutableFloat((float) base);
		EffectHolderHelper.forEachEffectHolder(weaponStack, (effectHolder, level) ->
			effectHolder.value().modifyAmmoUse(world, level, projectileStack, result)
		);
		return result.intValue();
	}

	@WrapMethod(method = "getBlockExperience")
	private static int getBlockExperience(ServerWorld world, ItemStack stack, int base, Operation<Integer> original) {
		MutableFloat result = new MutableFloat((float) base);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyBlockExperience(world, level, stack, result)
		);
		return result.intValue();
	}

	@WrapMethod(method = "getMobExperience")
	private static int getMobExperience(ServerWorld world, Entity attacker, Entity target, int base, Operation<Integer> original) {
		if (!(attacker instanceof LivingEntity living)) return base;

		MutableFloat result = new MutableFloat((float) base);
		EffectHolderHelper.forEachEffectHolder(living, (effectHolder, level, context) ->
			effectHolder.value().modifyMobExperience(world, level, context.stack(), target, result)
		);
		return result.intValue();
	}

	@WrapMethod(method = "isInvulnerableTo")
	private static boolean isInvulnerableTo(ServerWorld world, LivingEntity target, DamageSource source, Operation<Boolean> original) {
		MutableBoolean result = new MutableBoolean();
		EffectHolderHelper.forEachEffectHolder(target, (effectHolder, level, context) ->
			result.setValue(result.isTrue() || effectHolder.value().hasDamageImmunityTo(world, level, target, source))
		);
		return result.isTrue();
	}

	@WrapMethod(method = "getProtectionAmount")
	private static float getProtectionAmount(ServerWorld world, LivingEntity target, DamageSource source, Operation<Float> original) {
		MutableFloat result = new MutableFloat(0.0F);
		EffectHolderHelper.forEachEffectHolder(target, (effectHolder, level, context) ->
			effectHolder.value().modifyDamageProtection(world, level, context.stack(), target, source, result)
		);
		return result.floatValue();
	}

	@WrapMethod(method = "getDamage")
	private static float getDamage(ServerWorld world, ItemStack stack, Entity target, DamageSource source, float base, Operation<Float> original) {
		MutableFloat result = new MutableFloat(base);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyDamage(world, level, stack, target, source, result)
		);
		return result.floatValue();
	}

	@WrapMethod(method = "getSmashDamagePerFallenBlock")
	private static float getSmashDamagePerFallenBlock(ServerWorld world, ItemStack stack, Entity target, DamageSource source, float base, Operation<Float> original) {
		MutableFloat result = new MutableFloat(base);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifySmashDamagePerFallenBlock(world, level, stack, target, source, result)
		);
		return result.floatValue();
	}

	@WrapMethod(method = "getArmorEffectiveness")
	private static float getArmorEffectiveness(ServerWorld world, ItemStack stack, Entity target, DamageSource source, float base, Operation<Float> original) {
		MutableFloat result = new MutableFloat(base);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyArmorEffectiveness(world, level, stack, target, source, result)
		);
		return result.floatValue();
	}

	@WrapMethod(method = "modifyKnockback")
	private static float modifyKnockback(ServerWorld world, ItemStack stack, Entity target, DamageSource damageSource, float baseKnockback, Operation<Float> original) {
		MutableFloat result = new MutableFloat(baseKnockback);
		EffectHolderHelper.forEachEffectHolder((LivingEntity) damageSource.getAttacker(), (effectHolder, level, context) ->
			effectHolder.value().modifyKnockback(world, level, stack, target, damageSource, result)
		);
//        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
//			effectHolder.value().modifyKnockback(world, level, stack, target, damageSource, result)
//        );
		return result.floatValue();
	}

	@WrapMethod(method = "onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/item/ItemStack;)V")
	private static void onTargetDamaged(ServerWorld world, Entity target, DamageSource source, ItemStack stack, Operation<Void> original) {
		if (target instanceof LivingEntity livingTarget) {
			EffectHolderHelper.forEachEffectHolder(livingTarget,
				(effectHolder, level, context) ->
					effectHolder.value().onTargetDamaged(world, level, context, EnchantmentEffectTarget.VICTIM, target, source)
			);
		}

		Entity attacker = source.getAttacker();
		if (attacker instanceof LivingEntity livingAttacker) {
			EffectHolderHelper.forEachEffectHolder(stack, EquipmentSlot.MAINHAND, livingAttacker,
				(effectHolder, level, context) ->
					effectHolder.value().onTargetDamaged(world, level, context, EnchantmentEffectTarget.ATTACKER, target, source)
			);
		}
	}

	@WrapMethod(method = "applyLocationBasedEffects(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V")
	private static void applyLocationBasedEffects(ServerWorld world, LivingEntity target, Operation<Void> original) {
		EffectHolderHelper.forEachEffectHolder(target, (effectHolder, level, context) ->
			effectHolder.value().applyLocationBasedEffects(world, level, context, target)
		);
	}

	@WrapMethod(method = "applyLocationBasedEffects(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V")
	private static void applyLocationBasedEffects(ServerWorld world, ItemStack stack, LivingEntity target, EquipmentSlot slot, Operation<Void> original) {
		EffectHolderHelper.forEachEffectHolder(stack, slot, target, (effectHolder, level, context) ->
			effectHolder.value().applyLocationBasedEffects(world, level, context, target)
		);
	}

	@WrapMethod(method = "removeLocationBasedEffects(Lnet/minecraft/entity/LivingEntity;)V")
	private static void removeLocationBasedEffects(LivingEntity target, Operation<Void> original) {
		EffectHolderHelper.forEachEffectHolder(target, (effectHolder, level, context) ->
			effectHolder.value().removeLocationBasedEffects(level, context, target)
		);
	}

	@WrapMethod(method = "removeLocationBasedEffects(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V")
	private static void removeLocationBasedEffects(ItemStack stack, LivingEntity target, EquipmentSlot slot, Operation<Void> original) {
		EffectHolderHelper.forEachEffectHolder(stack, slot, target, (effectHolder, level, context) ->
			effectHolder.value().removeLocationBasedEffects(level, context, target)
		);
	}

	@WrapMethod(method = "onTick")
	private static void onTick(ServerWorld world, LivingEntity target, Operation<Void> original) {
		EffectHolderHelper.forEachEffectHolder(target, (effectHolder, level, context) ->
			effectHolder.value().onTick(world, level, context, target)
		);
	}

	@WrapMethod(method = "getProjectileCount")
	private static int getProjectileCount(ServerWorld world, ItemStack stack, Entity user, int base, Operation<Integer> original) {
		MutableFloat result = new MutableFloat((float) base);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyProjectileCount(world, level, stack, user, result)
		);
		return Math.max(0, result.intValue());
	}

	@WrapMethod(method = "getProjectileSpread")
	private static float getProjectileSpread(ServerWorld world, ItemStack stack, Entity target, float base, Operation<Float> original) {
		MutableFloat result = new MutableFloat(base);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyProjectileSpread(world, level, stack, target, result)
		);
		return Math.max(0.0F, result.floatValue());
	}

	@WrapMethod(method = "getProjectilePiercing")
	private static int getProjectilePiercing(ServerWorld world, ItemStack weaponStack, ItemStack projectileStack, Operation<Integer> original) {
		MutableFloat result = new MutableFloat(0.0F);
		EffectHolderHelper.forEachEffectHolder(weaponStack, (effectHolder, level) ->
			effectHolder.value().modifyProjectilePiercing(world, level, projectileStack, result)
		);
		return Math.max(0, result.intValue());
	}

	@WrapMethod(method = "onProjectileSpawned")
	private static void onProjectileSpawned(ServerWorld world, ItemStack stack, PersistentProjectileEntity projectileEntity, Consumer<Item> onBreak, Operation<Void> original) {
		Entity owner = projectileEntity.getOwner();
		if (!(owner instanceof LivingEntity entity)) return;

		EquipmentSlot slot = null;
		if (entity instanceof PlayerEntity player) {
			int rawSlot = player.getInventory().getSlotWithStack(stack);
			slot = PlayerEntity.getEquipmentSlot(rawSlot);
		}
		EffectHolderHelper.forEachEffectHolder(stack, slot, entity, (effectHolder, level, context) ->
			effectHolder.value().onProjectileSpawned(world, level, context, projectileEntity)
		);
	}

	@WrapMethod(method = "onHitBlock")
	private static void onHitBlock(ServerWorld world, ItemStack stack, LivingEntity user, Entity enchantedEntity, EquipmentSlot slot, Vec3d pos, BlockState state, Consumer<Item> onBreak, Operation<Void> original) {
		EffectHolderHelper.forEachEffectHolder(stack, slot, user, (effectHolder, level, context) ->
			effectHolder.value().onHitBlock(world, level, context, enchantedEntity, pos, state)
		);
	}

	@WrapMethod(method = "getRepairWithXp")
	private static int getRepairWithXp(ServerWorld world, ItemStack stack, int base, Operation<Integer> original) {
		MutableFloat result = new MutableFloat((float) base);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyRepairWithXp(world, level, stack, result)
		);
		return Math.max(0, result.intValue());
	}

	@WrapMethod(method = "applyAttributeModifiers(Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V")
	private static void applyAttributeModifiers(ItemStack stack, AttributeModifierSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> consumer, Operation<Void> original) {
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().getEffect(EnchantmentEffectComponentTypes.ATTRIBUTES).forEach(effect -> {
					if (effectHolder.value().getDefinition().contains(slot))
						consumer.accept(effect.attribute(), effect.createAttributeModifier(level, slot));
				}
			));
	}

	@WrapMethod(method = "applyAttributeModifiers(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V")
	private static void applyAttributeModifiers(ItemStack stack, EquipmentSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> consumer, Operation<Void> original) {
		EffectHolderHelper.forEachEffectHolder(stack, slot, (effectHolder, level) ->
			effectHolder.value().getEffect(EnchantmentEffectComponentTypes.ATTRIBUTES).forEach(effect ->
				consumer.accept(effect.attribute(), effect.createAttributeModifier(level, slot))
			));
	}

	@WrapMethod(method = "getFishingLuckBonus")
	private static int getFishingLuckBonus(ServerWorld world, ItemStack stack, Entity user, Operation<Integer> original) {
		MutableFloat result = new MutableFloat(0.0F);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyFishingLuckBonus(world, level, stack, user, result)
		);
		return Math.max(0, result.intValue());
	}

	@WrapMethod(method = "getFishingTimeReduction")
	private static float getFishingTimeReduction(ServerWorld world, ItemStack stack, Entity user, Operation<Float> original) {
		MutableFloat result = new MutableFloat(0.0F);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyFishingTimeReduction(world, level, stack, user, result)
		);
		return Math.max(0.0F, result.floatValue());
	}

	@WrapMethod(method = "getTridentReturnAcceleration")
	private static int getTridentReturnAcceleration(ServerWorld world, ItemStack stack, Entity user, Operation<Integer> original) {
		MutableFloat result = new MutableFloat(0.0F);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyTridentReturnAcceleration(world, level, stack, user, result)
		);
		return Math.max(0, result.intValue());
	}

	@WrapMethod(method = "getCrossbowChargeTime")
	private static float getCrossbowChargeTime(ItemStack stack, LivingEntity user, float base, Operation<Float> original) {
		MutableFloat result = new MutableFloat(base);
//		EquipmentSlot slot = user.getPreferredEquipmentSlot()
		EffectHolderHelper.forEachEffectHolder(user, (effectHolder, level, context) ->
			effectHolder.value().modifyCrossbowChargeTime(user.getRandom(), level, result)
		);
		return Math.max(0.0F, result.floatValue());
	}

	@WrapMethod(method = "getTridentSpinAttackStrength")
	private static float getTridentSpinAttackStrength(ItemStack stack, LivingEntity user, Operation<Float> original) {
		MutableFloat result = new MutableFloat(0.0F);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
			effectHolder.value().modifyTridentSpinAttackStrength(user.getRandom(), level, result)
		);
		return result.floatValue();
	}

	@WrapMethod(method = "hasAnyEnchantmentsWith")
	private static boolean hasAnyEnchantmentsWith(ItemStack stack, ComponentType<?> componentType, Operation<Boolean> original) {
		MutableBoolean result = new MutableBoolean(false);
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) -> {
			if (effectHolder.value().getEffects().contains(componentType)) result.setTrue();
		});
		return result.booleanValue();
	}

	@WrapMethod(method = "getEffectListAndLevel")
	@Nullable
	private static <T> Pair<T, Integer> getEffectListAndLevel(ItemStack stack, ComponentType<T> componentType, Operation<Pair<T, Integer>> original) {
		MutableObject<Pair<T, Integer>> result = new MutableObject<>();
		EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) -> {
			if (result.getValue() == null || result.getValue().getSecond() < level) {
				T t = effectHolder.value().getEffects().get(componentType);
				if (t != null) result.setValue(Pair.of(t, level));
			}
		});
		return result.getValue();
	}

}
