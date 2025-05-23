package org.solstice.euclidsElements.api.effectHolder.entity;

import net.minecraft.enchantment.effect.EnchantmentLocationBasedEffect;
import org.solstice.euclidsElements.api.effectHolder.EffectHolder;

import java.util.Map;
import java.util.Set;

public interface EntityEffectHolderData {

    default Map<EffectHolder, Set<EnchantmentLocationBasedEffect>> getLocationBasedEffects() {
		return Map.of();
	}

}
