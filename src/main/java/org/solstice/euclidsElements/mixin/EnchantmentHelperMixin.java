package org.solstice.euclidsElements.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.solstice.euclidsElements.api.effectHolder.EffectHolderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static void runIterationOnItem(ItemStack stack, EnchantmentHelper.EnchantmentVisitor consumer) {
        EffectHolderHelper.forEachEffectHolder(stack, consumer);
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static void runIterationOnItem(ItemStack stack, EquipmentSlot slot, LivingEntity entity, EnchantmentHelper.EnchantmentInSlotVisitor consumer) {
        EffectHolderHelper.forEachEffectHolder(stack, slot, entity, consumer);
    }


    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static int processDurabilityChange(ServerLevel world, ItemStack stack, int base) {
        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyItemDamage(world, level, stack, result)
        );
        return result.intValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static int processAmmoUse(ServerLevel world, ItemStack weaponStack, ItemStack projectileStack, int base) {
        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(weaponStack, (effectHolder, level) ->
                effectHolder.value().modifyAmmoUse(world, level, projectileStack, result)
        );
        return result.intValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static int processBlockExperience(ServerLevel world, ItemStack stack, int base) {
        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyBlockExperience(world, level, stack, result)
        );
        return result.intValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static int processMobExperience(ServerLevel world, @Nullable Entity attacker, Entity target, int base) {
        if (!(attacker instanceof LivingEntity living)) return base;

        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(living, (effectHolder, level, context) ->
                effectHolder.value().modifyMobExperience(world, level, context.itemStack(), target, result)
        );
        return result.intValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static boolean isImmuneToDamage(ServerLevel world, LivingEntity target, DamageSource source) {
        MutableBoolean result = new MutableBoolean();
        EffectHolderHelper.forEachEffectHolder(target, (effectHolder, level, context) ->
                result.setValue(result.isTrue() || effectHolder.value().hasDamageImmunityTo(world, level, target, source))
        );
        return result.isTrue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static float getDamageProtection(ServerLevel world, LivingEntity target, DamageSource source) {
        MutableFloat result = new MutableFloat(0.0F);
        EffectHolderHelper.forEachEffectHolder(target, (effectHolder, level, context) ->
                effectHolder.value().modifyDamageProtection(world, level, context.itemStack(), target, source, result)
        );
        return result.floatValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static float modifyDamage(ServerLevel world, ItemStack stack, Entity target, DamageSource source, float base) {
        MutableFloat result = new MutableFloat(base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyDamage(world, level, stack, target, source, result)
        );
        return result.floatValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static float modifyFallBasedDamage(ServerLevel world, ItemStack stack, Entity target, DamageSource source, float base) {
        MutableFloat result = new MutableFloat(base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifySmashDamagePerFallenBlock(world, level, stack, target, source, result)
        );
        return result.floatValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static float modifyArmorEffectiveness(ServerLevel world, ItemStack stack, Entity target, DamageSource source, float base) {
        MutableFloat result = new MutableFloat(base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyArmorEffectiveness(world, level, stack, target, source, result)
        );
        return result.floatValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static float modifyKnockback(ServerLevel world, ItemStack stack, Entity target, DamageSource damageSource, float baseKnockback) {
        MutableFloat result = new MutableFloat(baseKnockback);
        EffectHolderHelper.forEachEffectHolder(stack,(effectHolder, level) ->
                effectHolder.value().modifyKnockback(world, level, stack, target, damageSource, result)
        );
        return result.floatValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static void doPostAttackEffectsWithItemSource(ServerLevel world, Entity target, DamageSource source, @Nullable ItemStack stack) {
		if (target instanceof LivingEntity livingTarget) {
			EffectHolderHelper.forEachEffectHolder(livingTarget,
                (effectHolder, level, context) ->
                    effectHolder.value().onTargetDamaged(world, level, context, EnchantmentTarget.VICTIM, target, source)
            );
        }

        if (stack == null) return;

        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity livingAttacker) {
			EffectHolderHelper.forEachEffectHolder(stack, EquipmentSlot.MAINHAND, livingAttacker,
                (effectHolder, level, context) ->
                    effectHolder.value().onTargetDamaged(world, level, context, EnchantmentTarget.ATTACKER, target, source)
            );
        }
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static void runLocationChangedEffects(ServerLevel world, LivingEntity target) {
        EffectHolderHelper.forEachEffectHolder(target,
            (effectHolder, level, context) ->
                effectHolder.value().applyLocationBasedEffects(world, level, context, target)
        );
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static void runLocationChangedEffects(ServerLevel world, ItemStack stack, LivingEntity target, EquipmentSlot slot) {
        EffectHolderHelper.forEachEffectHolder(stack, slot, target,
            (effectHolder, level, context) ->
                effectHolder.value().applyLocationBasedEffects(world, level, context, target)
        );
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static void stopLocationBasedEffects(LivingEntity target) {
        EffectHolderHelper.forEachEffectHolder(target,
            (effectHolder, level, context) ->
                effectHolder.value().removeLocationBasedEffects(level, context, target)
        );
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static void stopLocationBasedEffects(ItemStack stack, LivingEntity target, EquipmentSlot slot) {
        EffectHolderHelper.forEachEffectHolder(stack, slot, target,
            (effectHolder, level, context) ->
                effectHolder.value().removeLocationBasedEffects(level, context, target)
        );
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static void tickEffects(ServerLevel world, LivingEntity target) {
        EffectHolderHelper.forEachEffectHolder(target,
            (effectHolder, level, context) ->
                effectHolder.value().onTick(world, level, context, target)
        );
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static int processProjectileCount(ServerLevel world, ItemStack stack, Entity user, int base) {
        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyProjectileCount(world, level, stack, user, result)
        );
        return Math.max(0, result.intValue());
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static float processProjectileSpread(ServerLevel world, ItemStack stack, Entity target, float base) {
        MutableFloat result = new MutableFloat(base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyProjectileSpread(world, level, stack, target, result)
        );
        return Math.max(0.0F, result.floatValue());
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static int getPiercingCount(ServerLevel world, ItemStack weaponStack, ItemStack projectileStack) {
        MutableFloat result = new MutableFloat(0.0F);
        EffectHolderHelper.forEachEffectHolder(weaponStack, (effectHolder, level) ->
                effectHolder.value().modifyProjectilePiercing(world, level, projectileStack, result)
        );
        return Math.max(0, result.intValue());
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static void onProjectileSpawned(ServerLevel world, ItemStack stack, AbstractArrow projectileEntity, Consumer<Item> onBreak) {
        Entity owner = projectileEntity.getOwner();
        LivingEntity livingOwner = owner instanceof LivingEntity ? (LivingEntity) owner : null;

		EnchantedItemInUse context = new EnchantedItemInUse(stack, null, livingOwner, onBreak);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().onProjectileSpawned(world, level, context, projectileEntity)
        );
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static void onHitBlock(ServerLevel world, ItemStack stack, @Nullable LivingEntity user, Entity enchantedEntity, @Nullable EquipmentSlot slot, Vec3 pos, BlockState state, Consumer<Item> onBreak) {
		EnchantedItemInUse context = new EnchantedItemInUse(stack, slot, user, onBreak);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().onHitBlock(world, level, context, enchantedEntity, pos, state)
        );
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static int modifyDurabilityToRepairFromXp(ServerLevel world, ItemStack stack, int base) {
        MutableFloat result = new MutableFloat((float)base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyRepairWithXp(world, level, stack, result)
        );
        return Math.max(0, result.intValue());
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static float processEquipmentDropChance(ServerLevel world, LivingEntity target, DamageSource source, float base) {
        MutableFloat result = new MutableFloat(base);

        RandomSource random = target.getRandom();
        EffectHolderHelper.forEachEffectHolder(target, (effectHolder, level, context) -> {
            LootContext lootcontext = Enchantment.damageContext(world, level, target, source);
            effectHolder.value().getEffect(EnchantmentEffectComponents.EQUIPMENT_DROPS).forEach((effect) -> {
                if (effect.enchanted() == EnchantmentTarget.VICTIM && effect.affected() == EnchantmentTarget.VICTIM && effect.matches(lootcontext))
                    result.setValue(effect.effect().process(level, random, result.floatValue()));
            });
        });
        Entity attacker = source.getEntity();
        if (attacker instanceof LivingEntity livingAttacker) {
            EffectHolderHelper.forEachEffectHolder(livingAttacker, (effectHolder, level, context) -> {
                LootContext lootcontext = Enchantment.damageContext(world, level, target, source);
                effectHolder.value().getEffect(EnchantmentEffectComponents.EQUIPMENT_DROPS).forEach((effect) -> {
                    if (effect.enchanted() == EnchantmentTarget.ATTACKER && effect.affected() == EnchantmentTarget.VICTIM && effect.matches(lootcontext))
                        result.setValue(effect.effect().process(level, random, result.floatValue()));
                });
            });
        }

        return result.floatValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
	public static void forEachModifier(ItemStack stack, EquipmentSlotGroup slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
            effectHolder.value().getEffect(EnchantmentEffectComponents.ATTRIBUTES).forEach(effect -> {
                if (effectHolder.value().getDefinition().getSlots().contains(slot))
                    consumer.accept(effect.attribute(), effect.getModifier(level, slot));
            }
        ));
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
	public static void forEachModifier(ItemStack stack, EquipmentSlot slot, BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
            effectHolder.value().getEffect(EnchantmentEffectComponents.ATTRIBUTES).forEach(effect -> {
                if (effectHolder.value().slotMatches(slot))
                    consumer.accept(effect.attribute(), effect.getModifier(level, slot));
            }
        ));
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static int getFishingLuckBonus(ServerLevel world, ItemStack stack, Entity user) {
        MutableFloat result = new MutableFloat(0.0F);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyFishingLuckBonus(world, level, stack, user, result)
        );
        return Math.max(0, result.intValue());
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static float getFishingTimeReduction(ServerLevel world, ItemStack stack, Entity user) {
        MutableFloat result = new MutableFloat(0.0F);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyFishingTimeReduction(world, level, stack, user, result)
        );
        return Math.max(0.0F, result.floatValue());
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static int getTridentReturnToOwnerAcceleration(ServerLevel world, ItemStack stack, Entity user) {
        MutableFloat result = new MutableFloat(0.0F);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyTridentReturnAcceleration(world, level, stack, user, result)
        );
        return Math.max(0, result.intValue());
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static float modifyCrossbowChargingTime(ItemStack stack, LivingEntity user, float base) {
        MutableFloat result = new MutableFloat(base);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyCrossbowChargeTime(user.getRandom(), level, result)
        );
        return Math.max(0.0F, result.floatValue());
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static float getTridentSpinAttackStrength(ItemStack stack, LivingEntity user) {
        MutableFloat result = new MutableFloat(0.0F);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) ->
                effectHolder.value().modifyTridentSpinAttackStrength(user.getRandom(), level, result)
        );
        return result.floatValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite
    public static boolean has(ItemStack stack, DataComponentType<?> componentType) {
        MutableBoolean result = new MutableBoolean(false);
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) -> {
            if (effectHolder.value().getEffects().has(componentType)) result.setTrue();
        });
        return result.booleanValue();
    }

    /**
     * @author Solstice
     * @reason Use EffectHolderHelper
     */
    @Overwrite @Nullable
    public static <T> Pair<T, Integer> getHighestLevel(ItemStack stack, DataComponentType<T> componentType) {
        MutableObject<Pair<T, Integer>> result = new MutableObject<>();
        EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) -> {
            if (result.getValue() == null || result.getValue().getSecond() < level) {
                T t = effectHolder.value().getEffects().get(componentType);
                if (t != null) result.setValue(Pair.of(t, level));
            }
        });
        return result.getValue();
    }

	/**
	 * @author Solstice
	 * @reason Use EffectHolderHelper
	 */
	@Overwrite
	public static Optional<EnchantedItemInUse> getRandomItemWith(DataComponentType<?> componentType, LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		List<EnchantedItemInUse> result = new ArrayList<>();

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			ItemStack stack = entity.getItemBySlot(slot);
			if (!stackPredicate.test(stack)) continue;

			EffectHolderHelper.forEachEffectHolder(stack, (effectHolder, level) -> {
				if (effectHolder.value().getEffects().has(componentType) && effectHolder.value().slotMatches(slot))
					result.add(new EnchantedItemInUse(stack, slot, entity));
			});
		}

		return Util.getRandomSafe(result, entity.getRandom());
	}

}
