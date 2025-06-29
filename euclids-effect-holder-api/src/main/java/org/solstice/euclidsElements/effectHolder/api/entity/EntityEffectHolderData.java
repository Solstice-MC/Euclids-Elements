package org.solstice.euclidsElements.effectHolder.api.entity;

import net.minecraft.enchantment.effect.EnchantmentLocationBasedEffect;
import org.solstice.euclidsElements.effectHolder.api.EffectHolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EntityEffectHolderData {

	default List<EffectHolder> getEffectHolders() {
		return List.of();
	}

    default Map<EffectHolder, Set<EnchantmentLocationBasedEffect>> getLocationBasedEffects() {
		return Map.of();
	}

}
