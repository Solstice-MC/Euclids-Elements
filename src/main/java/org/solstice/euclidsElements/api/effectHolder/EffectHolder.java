package org.solstice.euclidsElements.api.effectHolder;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.DamageImmunity;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.solstice.euclidsElements.registry.EuclidsEnchantmentEffects;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface EffectHolder {

	DataComponentMap getEffects();

    Definition getDefinition();

    default boolean slotMatches(EquipmentSlot equipmentSlot) {
        return this.getDefinition().getSlots().stream().anyMatch(slot -> slot.test(equipmentSlot));
    }

    static <T> void applyEffects(List<ConditionalEffect<T>> entries, LootContext context, Consumer<T> effectConsumer) {
        entries.forEach(effect -> {
            if (effect.matches(context)) {
                effectConsumer.accept(effect.effect());
            }
        });
    }

    default <T> List<T> getEffect(DataComponentType<List<T>> type) {
        return this.getEffects().getOrDefault(type, List.of());
    }


    default void onTick(ServerLevel world, int level, EnchantedItemInUse context, Entity user) {
        applyEffects(
                this.getEffect(EnchantmentEffectComponents.TICK),
                Enchantment.entityContext(world, level, user, user.position()),
                effect -> effect.apply(world, level, context, user, user.position())
        );
    }

    default void onProjectileSpawned(ServerLevel world, int level, EnchantedItemInUse context, Entity user) {
        applyEffects(
                this.getEffect(EnchantmentEffectComponents.PROJECTILE_SPAWNED),
                Enchantment.entityContext(world, level, user, user.position()),
                effect -> effect.apply(world, level, context, user, user.position())
        );
    }

    default void onHitBlock(ServerLevel world, int level, EnchantedItemInUse context, Entity enchantedEntity, Vec3 pos, BlockState state) {
        applyEffects(
                this.getEffect(EnchantmentEffectComponents.HIT_BLOCK),
                Enchantment.blockHitContext(world, level, enchantedEntity, pos, state),
                effect -> effect.apply(world, level, context, enchantedEntity, pos)
        );
    }

    default void modifyValue(DataComponentType<EnchantmentValueEffect> type, RandomSource random, int level, MutableFloat value) {
        EnchantmentValueEffect effect = this.getEffects().get(type);
        if (effect != null) {
            value.setValue(effect.process(level, random, value.floatValue()));
        }
    }

    default void modifyValue(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> type, ServerLevel world, int level, ItemStack stack, MutableFloat value) {
        applyEffects(
                this.getEffect(type),
                Enchantment.itemContext(world, level, stack),
                effect -> value.setValue(effect.process(level, world.getRandom(), value.getValue()))
        );
    }

    default void modifyValue(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> type, ServerLevel world, int level, ItemStack stack, Entity user, MutableFloat value) {
        applyEffects(
                this.getEffect(type),
                Enchantment.entityContext(world, level, user, user.position()),
                effect -> value.setValue(effect.process(level, user.getRandom(), value.floatValue()))
        );
    }

    default void modifyValue(DataComponentType<List<ConditionalEffect<EnchantmentValueEffect>>> type, ServerLevel world, int level, ItemStack stack, Entity user, DamageSource damageSource, MutableFloat value) {
        applyEffects(
                this.getEffect(type),
                Enchantment.damageContext(world, level, user, damageSource),
                effect -> value.setValue(effect.process(level, user.getRandom(), value.floatValue()))
        );
    }

    default boolean hasDamageImmunityTo(ServerLevel world, int level, Entity user, DamageSource damageSource) {
        LootContext context = Enchantment.damageContext(world, level, user, damageSource);

        for(ConditionalEffect<DamageImmunity> effect : this.getEffect(EnchantmentEffectComponents.DAMAGE_IMMUNITY)) {
            if (effect.matches(context)) return true;
        }
        return false;
    }

    default void modifyDamageProtection(ServerLevel world, int level, ItemStack stack, Entity user, DamageSource damageSource, MutableFloat damageProtection) {
        LootContext context = Enchantment.damageContext(world, level, user, damageSource);

        for(ConditionalEffect<EnchantmentValueEffect> effect : this.getEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION)) {
            if (effect.matches(context)) damageProtection.setValue(
                    effect.effect().process(level, user.getRandom(), damageProtection.floatValue())
            );
        }
    }

    default void modifyItemDamage(ServerLevel world, int level, ItemStack stack, MutableFloat itemDamage) {
        this.modifyValue(EnchantmentEffectComponents.ITEM_DAMAGE, world, level, stack, itemDamage);
    }

    default void modifyAmmoUse(ServerLevel world, int level, ItemStack projectileStack, MutableFloat ammoUse) {
        this.modifyValue(EnchantmentEffectComponents.AMMO_USE, world, level, projectileStack, ammoUse);
    }

    default void onTargetDamaged(ServerLevel world, int level, EnchantedItemInUse context, EnchantmentTarget target, Entity user, DamageSource damageSource) {
        for (TargetedConditionalEffect<EnchantmentEntityEffect> effect : this.getEffect(EnchantmentEffectComponents.POST_ATTACK)) {
            if (target == effect.enchanted())
                Enchantment.doPostAttack(effect, world, level, context, user, damageSource);
        }
    }

    default void modifyProjectilePiercing(ServerLevel world, int level, ItemStack stack, MutableFloat projectilePiercing) {
        this.modifyValue(EnchantmentEffectComponents.PROJECTILE_PIERCING, world, level, stack, projectilePiercing);
    }

    default void modifyBlockExperience(ServerLevel world, int level, ItemStack stack, MutableFloat blockExperience) {
        this.modifyValue(EnchantmentEffectComponents.BLOCK_EXPERIENCE, world, level, stack, blockExperience);
    }

    default void modifyMobExperience(ServerLevel world, int level, ItemStack stack, Entity user, MutableFloat mobExperience) {
        this.modifyValue(EnchantmentEffectComponents.MOB_EXPERIENCE, world, level, stack, user, mobExperience);
    }

    default void modifyRepairWithXp(ServerLevel world, int level, ItemStack stack, MutableFloat repairWithXp) {
        this.modifyValue(EnchantmentEffectComponents.REPAIR_WITH_XP, world, level, stack, repairWithXp);
    }

    default void modifyTridentReturnAcceleration(ServerLevel world, int level, ItemStack stack, Entity user, MutableFloat tridentReturnAcceleration) {
        this.modifyValue(EnchantmentEffectComponents.TRIDENT_RETURN_ACCELERATION, world, level, stack, user, tridentReturnAcceleration);
    }

    default void modifyTridentSpinAttackStrength(RandomSource random, int level, MutableFloat tridentSpinAttackStrength) {
        this.modifyValue(EnchantmentEffectComponents.TRIDENT_SPIN_ATTACK_STRENGTH, random, level, tridentSpinAttackStrength);
    }

    default void modifyFishingTimeReduction(ServerLevel world, int level, ItemStack stack, Entity user, MutableFloat fishingTimeReduction) {
        this.modifyValue(EnchantmentEffectComponents.FISHING_TIME_REDUCTION, world, level, stack, user, fishingTimeReduction);
    }

    default void modifyFishingLuckBonus(ServerLevel world, int level, ItemStack stack, Entity user, MutableFloat fishingLuckBonus) {
        this.modifyValue(EnchantmentEffectComponents.FISHING_LUCK_BONUS, world, level, stack, user, fishingLuckBonus);
    }

    default void modifyDamage(ServerLevel world, int level, ItemStack stack, Entity user, DamageSource damageSource, MutableFloat damage) {
        this.modifyValue(EnchantmentEffectComponents.DAMAGE, world, level, stack, user, damageSource, damage);
    }

    default void modifySmashDamagePerFallenBlock(ServerLevel world, int level, ItemStack stack, Entity user, DamageSource damageSource, MutableFloat smashDamagePerFallenBlock) {
        this.modifyValue(EnchantmentEffectComponents.SMASH_DAMAGE_PER_FALLEN_BLOCK, world, level, stack, user, damageSource, smashDamagePerFallenBlock);
    }

    default void modifyKnockback(ServerLevel world, int level, ItemStack stack, Entity user, DamageSource damageSource, MutableFloat knockback) {
        this.modifyValue(EnchantmentEffectComponents.KNOCKBACK, world, level, stack, user, damageSource, knockback);
    }

    default void modifyArmorEffectiveness(ServerLevel world, int level, ItemStack stack, Entity user, DamageSource damageSource, MutableFloat armorEffectiveness) {
        this.modifyValue(EnchantmentEffectComponents.ARMOR_EFFECTIVENESS, world, level, stack, user, damageSource, armorEffectiveness);
    }

    default void modifyProjectileCount(ServerLevel world, int level, ItemStack stack, Entity user, MutableFloat projectileCount) {
        this.modifyValue(EnchantmentEffectComponents.PROJECTILE_COUNT, world, level, stack, user, projectileCount);
    }

    default void modifyProjectileSpread(ServerLevel world, int level, ItemStack stack, Entity user, MutableFloat projectileSpread) {
        this.modifyValue(EnchantmentEffectComponents.PROJECTILE_SPREAD, world, level, stack, user, projectileSpread);
    }

    default void modifyCrossbowChargeTime(RandomSource random, int level, MutableFloat crossbowChargeTime) {
        this.modifyValue(EnchantmentEffectComponents.CROSSBOW_CHARGE_TIME, random, level, crossbowChargeTime);
    }

	default void modifyMaxDurability(int level, MutableFloat maxDurability) {
		this.modifyValue(EuclidsEnchantmentEffects.MAX_DURABILITY, RandomSource.create(), level, maxDurability);
	}


    default void applyLocationBasedEffects(ServerLevel world, int level, EnchantedItemInUse context, LivingEntity user) {
		Set<EnchantmentLocationBasedEffect> locationEffects = user.activeLocationDependentEnchantments().remove(this);
        if (context.inSlot() != null && !this.slotMatches(context.inSlot())) {
			if (locationEffects != null) locationEffects.forEach(effect -> effect.onDeactivated(context, user, user.position(), level));
			return;
		}

		for (ConditionalEffect<EnchantmentLocationBasedEffect> effect : this.getEffect(EnchantmentEffectComponents.LOCATION_CHANGED)) {
			EnchantmentLocationBasedEffect locationEffect = effect.effect();
			boolean flag = locationEffects != null && locationEffects.contains(locationEffect);
			if (effect.matches(Enchantment.locationContext(world, level, user, flag))) {
				if (!flag) {
					if (locationEffects == null) {
						locationEffects = new ObjectArraySet<>();
						user.activeLocationDependentEnchantments().put(this, locationEffects);
					}

					locationEffects.add(locationEffect);
				}

				locationEffect.onChangedBlock(world, level, context, user, user.position(), !flag);
			} else if (locationEffects != null && locationEffects.remove(locationEffect)) {
				locationEffect.onDeactivated(context, user, user.position(), level);
			}
		}

		if (locationEffects != null && locationEffects.isEmpty()) user.activeLocationDependentEnchantments().remove(this);
    }

    default void removeLocationBasedEffects(int level, EnchantedItemInUse context, LivingEntity user) {
        Set<EnchantmentLocationBasedEffect> set = user.activeLocationDependentEnchantments().remove(this);
        if (set == null) return;

        for (EnchantmentLocationBasedEffect effect : set) {
            effect.onDeactivated(context, user, user.position(), level);
        }
    }

    interface Definition {
        int getMaxLevel();
        List<EquipmentSlotGroup> getSlots();
    }

}
