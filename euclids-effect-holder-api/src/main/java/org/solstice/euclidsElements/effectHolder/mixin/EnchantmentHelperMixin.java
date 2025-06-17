package org.solstice.euclidsElements.effectHolder.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentType;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.effectHolder.api.EffectHolderHelper;
import org.solstice.euclidsElements.effectHolder.api.component.EffectHolderComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

	@Shadow
	public static void forEachEnchantment(LivingEntity entity, EnchantmentHelper.ContextAwareConsumer consumer) {
		throw new AssertionError();
	}

	@Shadow
	public static void forEachEnchantment(ItemStack stack, EnchantmentHelper.Consumer consumer) {
		throw new AssertionError();
	}

	@Shadow
	public static void forEachEnchantment(ItemStack stack, EquipmentSlot slot, LivingEntity entity, EnchantmentHelper.ContextAwareConsumer consumer) {
		throw new AssertionError();
	}

	@WrapMethod(method = "forEachEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/enchantment/EnchantmentHelper$Consumer;)V")
	@SuppressWarnings("unchecked")
	private static void wrapForEachEnchantment(ItemStack stack, EnchantmentHelper.Consumer consumer, Operation<Void> original) {
		EffectHolderHelper.EFFECT_HOLDER_COMPONENTS.forEach(componentType -> {
			EffectHolderComponent<?> component = stack.getOrDefault(componentType, null);
			if (component == null) return;

			ItemEnchantmentsComponent monkey;
			if (component instanceof ItemEnchantmentsComponent) {
				monkey = (ItemEnchantmentsComponent) component;
				// noinspection DataFlowIssue
				component = monkey;
			}

			component.getEffects().forEach((entry, level) -> {
				consumer.accept((RegistryEntry<Enchantment>) entry, level);
			});
		});
    }

    @WrapMethod(method = "forEachEnchantment(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/enchantment/EnchantmentHelper$ContextAwareConsumer;)V")
	@SuppressWarnings("unchecked")
	private static void wrapForEachEnchantment(ItemStack stack, EquipmentSlot slot, LivingEntity entity, EnchantmentHelper.ContextAwareConsumer consumer, Operation<Void> original) {
		if (stack.isEmpty()) return;

		EffectHolderHelper.EFFECT_HOLDER_COMPONENTS.forEach(componentType -> {
			EffectHolderComponent<?> component = stack.getOrDefault(componentType, null);
			if (component == null) return;

			ItemEnchantmentsComponent monkey;
			if (component instanceof ItemEnchantmentsComponent) {
				monkey = (ItemEnchantmentsComponent) component;
				// noinspection DataFlowIssue
				component = monkey;
			}

			EnchantmentEffectContext context = new EnchantmentEffectContext(stack, slot, entity);
			component.getEffects().forEach((entry, level) -> {
				if (entry.value().slotMatches(slot)) {
					consumer.accept((RegistryEntry<Enchantment>) entry, level, context);
				}
			});
		});
    }

	@WrapMethod(method = "getEquipmentDropChance")
	private static float getEquipmentDropChance(ServerWorld world, LivingEntity target, DamageSource source, float base, Operation<Float> original) {
		MutableFloat result = new MutableFloat(base);

		Random random = target.getRandom();
		EffectHolderHelper.forEachEffectHolder(target, (effectHolder, level, context) -> {
			LootContext lootcontext = Enchantment.createEnchantedDamageLootContext(world, level, target, source);
			effectHolder.value().getEffect(EnchantmentEffectComponentTypes.EQUIPMENT_DROPS).forEach((effect) -> {
				if (effect.enchanted() == EnchantmentEffectTarget.VICTIM && effect.affected() == EnchantmentEffectTarget.VICTIM && effect.test(lootcontext))
					result.setValue(effect.effect().apply(level, random, result.floatValue()));
			});
		});
		Entity attacker = source.getAttacker();
		if (attacker instanceof LivingEntity livingAttacker) {
			forEachEnchantment(livingAttacker, (effectHolder, level, context) -> {
				LootContext lootcontext = Enchantment.createEnchantedDamageLootContext(world, level, target, source);
				effectHolder.value().getEffect(EnchantmentEffectComponentTypes.EQUIPMENT_DROPS).forEach((effect) -> {
					if (effect.enchanted() == EnchantmentEffectTarget.ATTACKER && effect.affected() == EnchantmentEffectTarget.VICTIM && effect.test(lootcontext))
						result.setValue(effect.effect().apply(level, random, result.floatValue()));
				});
			});
		}

		return result.floatValue();
	}

	@WrapMethod(method = "chooseEquipmentWith")
	private static Optional<EnchantmentEffectContext> chooseEquipmentWith(ComponentType<?> componentType, LivingEntity entity, Predicate<ItemStack> stackPredicate, Operation<Optional<EnchantmentEffectContext>> original) {
		List<EnchantmentEffectContext> result = new ArrayList<>();

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack stack = entity.getEquippedStack(slot);
			if (!stackPredicate.test(stack)) continue;

			EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) -> {
				if (effectHolder.value().getEffects().contains(componentType) && effectHolder.value().slotMatches(slot))
					result.add(new EnchantmentEffectContext(stack, slot, entity));
			});
		}

		return Util.getRandomOrEmpty(result, entity.getRandom());
	}

	@WrapMethod(method = "getItemDamage")
	private static int getItemDamage(ServerWorld world, ItemStack stack, int base, Operation<Integer> original) {
        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyItemDamage(world, level, stack, result)
        );
        return result.intValue();
    }

	@WrapMethod(method = "getAmmoUse")
	private static int getAmmoUse(ServerWorld world, ItemStack weaponStack, ItemStack projectileStack, int base, Operation<Integer> original) {
        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(weaponStack, (effectHolder, level) ->
                effectHolder.value().modifyAmmoUse(world, level, projectileStack, result)
        );
        return result.intValue();
    }

	@WrapMethod(method = "getBlockExperience")
	private static int getBlockExperience(ServerWorld world, ItemStack stack, int base, Operation<Integer> original) {
        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyBlockExperience(world, level, stack, result)
        );
        return result.intValue();
    }

	@WrapMethod(method = "getMobExperience")
	private static int getMobExperience(ServerWorld world, Entity attacker, Entity target, int base, Operation<Integer> original) {
        if (!(attacker instanceof LivingEntity living)) return base;

        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(living, (effectHolder, level, context) ->
                effectHolder.value().modifyMobExperience(world, level, context.stack(), target, result)
        );
        return result.intValue();
    }

	@WrapMethod(method = "isInvulnerableTo")
	private static boolean isInvulnerableTo(ServerWorld world, LivingEntity target, DamageSource source, Operation<Boolean> original) {
        MutableBoolean result = new MutableBoolean();
		forEachEnchantment(target, (effectHolder, level, context) ->
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
        EffectHolderHelper.forEachEffectHolder(stack,(effectHolder, level) ->
                effectHolder.value().modifyKnockback(world, level, stack, target, damageSource, result)
        );
        return result.floatValue();
    }

	@WrapMethod(method = "onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/item/ItemStack;)V")
    private static void onTargetDamaged(ServerWorld world, Entity target, DamageSource source, ItemStack stack, Operation<Void> original) {
		if (target instanceof LivingEntity livingTarget) {
			forEachEnchantment(livingTarget,
                (effectHolder, level, context) ->
                    effectHolder.value().onTargetDamaged(world, level, context, EnchantmentEffectTarget.VICTIM, target, source)
            );
        }

        if (stack == null) return;

        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity livingAttacker) {
			forEachEnchantment(stack, EquipmentSlot.MAINHAND, livingAttacker,
                (effectHolder, level, context) ->
                    effectHolder.value().onTargetDamaged(world, level, context, EnchantmentEffectTarget.ATTACKER, target, source)
            );
        }
    }

	@WrapMethod(method = "applyLocationBasedEffects(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;)V")
    private static void applyLocationBasedEffects(ServerWorld world, LivingEntity target, Operation<Void> original) {
        EffectHolderHelper.forEachEffectHolder(target,
            (effectHolder, level, context) ->
                effectHolder.value().applyLocationBasedEffects(world, level, context, target)
        );
    }

	@WrapMethod(method = "applyLocationBasedEffects(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V")
    private static void applyLocationBasedEffects(ServerWorld world, ItemStack stack, LivingEntity target, EquipmentSlot slot, Operation<Void> original) {
        EffectHolderHelper.forEachEffectHolder(stack, slot, target,
            (effectHolder, level, context) ->
                effectHolder.value().applyLocationBasedEffects(world, level, context, target)
        );
    }

	@WrapMethod(method = "removeLocationBasedEffects(Lnet/minecraft/entity/LivingEntity;)V")
    private static void removeLocationBasedEffects(LivingEntity target, Operation<Void> original) {
        EffectHolderHelper.forEachEffectHolder(target,
            (effectHolder, level, context) ->
                effectHolder.value().removeLocationBasedEffects(level, context, target)
        );
    }

	@WrapMethod(method = "removeLocationBasedEffects(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V")
    private static void removeLocationBasedEffects(ItemStack stack, LivingEntity target, EquipmentSlot slot, Operation<Void> original) {
        EffectHolderHelper.forEachEffectHolder(stack, slot, target,
            (effectHolder, level, context) ->
                effectHolder.value().removeLocationBasedEffects(level, context, target)
        );
    }

	@WrapMethod(method = "onTick")
    private static void onTick(ServerWorld world, LivingEntity target, Operation<Void> original) {
        EffectHolderHelper.forEachEffectHolder(target,
            (effectHolder, level, context) ->
                effectHolder.value().onTick(world, level, context, target)
        );
    }

	@WrapMethod(method = "getProjectileCount")
	private static int getProjectileCount(ServerWorld world, ItemStack stack, Entity user, int base, Operation<Integer> original) {
        MutableFloat result = new MutableFloat((float)base);
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
        LivingEntity livingOwner = owner instanceof LivingEntity ? (LivingEntity) owner : null;

        EnchantmentEffectContext context = new EnchantmentEffectContext(stack, null, livingOwner, onBreak);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().onProjectileSpawned(world, level, context, projectileEntity)
        );
    }

	@WrapMethod(method = "onHitBlock")
    private static void onHitBlock(ServerWorld world, ItemStack stack, LivingEntity user, Entity enchantedEntity, EquipmentSlot slot, Vec3d pos, BlockState state, Consumer<Item> onBreak, Operation<Void> original) {
        EnchantmentEffectContext context = new EnchantmentEffectContext(stack, slot, user, onBreak);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().onHitBlock(world, level, context, enchantedEntity, pos, state)
        );
    }

	@WrapMethod(method = "getRepairWithXp")
	private static int getRepairWithXp(ServerWorld world, ItemStack stack, int base, Operation<Integer> original) {
        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyRepairWithXp(world, level, stack, result)
        );
        return Math.max(0, result.intValue());
    }

	@WrapMethod(method = "applyAttributeModifiers(Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/type/AttributeModifierSlot;Ljava/util/function/BiConsumer;)V")
	private static void applyAttributeModifiers(ItemStack stack, AttributeModifierSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> consumer, Operation<Void> original) {
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
            effectHolder.value().getEffect(EnchantmentEffectComponentTypes.ATTRIBUTES).forEach(effect -> {
                if (effectHolder.value().getDefinition().getSlots().contains(slot))
                    consumer.accept(effect.attribute(), effect.createAttributeModifier(level, slot));
            }
        ));
    }

	@WrapMethod(method = "applyAttributeModifiers(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V")
	private static void applyAttributeModifiers(ItemStack stack, EquipmentSlot slot, BiConsumer<RegistryEntry<EntityAttribute>, EntityAttributeModifier> consumer, Operation<Void> original) {
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
            effectHolder.value().getEffect(EnchantmentEffectComponentTypes.ATTRIBUTES).forEach(effect -> {
                if (effectHolder.value().slotMatches(slot))
                    consumer.accept(effect.attribute(), effect.createAttributeModifier(level, slot));
            }
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
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
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
