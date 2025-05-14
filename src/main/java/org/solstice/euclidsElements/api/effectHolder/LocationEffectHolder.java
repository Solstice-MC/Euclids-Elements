package org.solstice.euclidsElements.api.effectHolder;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Set;

public interface LocationEffectHolder extends AbstractEffectHolder {

    Definition getDefinition();

	default boolean slotMatches(EquipmentSlot equipmentSlot) {
		return this.getDefinition().getSlots().stream().anyMatch(slot -> slot.matches(equipmentSlot));
	}

    default void applyLocationBasedEffects(ServerWorld world, int level, EnchantmentEffectContext context, LivingEntity user) {
		Set<EnchantmentLocationBasedEffect> locationEffects = user.getLocationBasedEffects().remove(this);
        if (context.slot() != null && !this.slotMatches(context.slot())) {
			if (locationEffects != null) locationEffects.forEach(effect -> effect.remove(context, user, user.getPos(), level));
			return;
		}

		for (EnchantmentEffectEntry<EnchantmentLocationBasedEffect> effect : this.getEffect(EnchantmentEffectComponentTypes.LOCATION_CHANGED)) {
			EnchantmentLocationBasedEffect locationEffect = effect.effect();
			boolean flag = locationEffects != null && locationEffects.contains(locationEffect);
			if (effect.test(Enchantment.createEnchantedLocationLootContext(world, level, user, flag))) {
				if (!flag) {
					if (locationEffects == null) {
						locationEffects = new ObjectArraySet<>();
						// TODO TODO TODO TODO TODO TODO TODO TODO
//						user.getLocationBasedEffects().put(this, locationEffects);
					}

					locationEffects.add(locationEffect);
				}

				locationEffect.apply(world, level, context, user, user.getPos(), !flag);
			} else if (locationEffects != null && locationEffects.remove(locationEffect)) {
				locationEffect.remove(context, user, user.getPos(), level);
			}
		}

		if (locationEffects != null && locationEffects.isEmpty()) user.getLocationBasedEffects().remove(this);
    }

    default void removeLocationBasedEffects(int level, EnchantmentEffectContext context, LivingEntity user) {
        Set<EnchantmentLocationBasedEffect> set = user.getLocationBasedEffects().remove(this);
        if (set == null) return;

        for(EnchantmentLocationBasedEffect effect : set) {
            effect.remove(context, user, user.getPos(), level);
        }
    }

    interface Definition extends AbstractEffectHolder.Definition {
        List<AttributeModifierSlot> getSlots();
    }

}
