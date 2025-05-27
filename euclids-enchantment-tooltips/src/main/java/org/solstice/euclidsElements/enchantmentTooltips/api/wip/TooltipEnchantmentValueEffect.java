package org.solstice.euclidsElements.enchantmentTooltips.api.wip;

import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.registry.Registries;

public interface TooltipEnchantmentValueEffect {

	default String getTranslationKey() {
		return Registries.ENCHANTMENT_VALUE_EFFECT_TYPE.getEntry(((EnchantmentValueEffect)this).getCodec())
			.getKey().orElseThrow().getValue().toTranslationKey("enchantment_value_effect");
	}

}
