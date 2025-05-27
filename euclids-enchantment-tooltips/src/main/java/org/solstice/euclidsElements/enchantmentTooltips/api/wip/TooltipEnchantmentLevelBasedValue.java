package org.solstice.euclidsElements.enchantmentTooltips.api.wip;

import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.registry.Registries;

public interface TooltipEnchantmentLevelBasedValue {

	default String getTranslationKey() {
		return Registries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE.getEntry(((EnchantmentLevelBasedValue)this).getCodec())
			.getKey().orElseThrow().getValue().toTranslationKey("enchantment_level_based_value");
	}

}
