package org.solstice.euclidsElements.enchantmentTooltips.api.wip;

import net.minecraft.enchantment.provider.EnchantmentProvider;
import net.minecraft.registry.Registries;

public interface TooltipEnchantmentProvider {

	default String getTranslationKey() {
		return Registries.ENCHANTMENT_PROVIDER_TYPE.getEntry(((EnchantmentProvider)this).getCodec())
			.getKey().orElseThrow().getValue().toTranslationKey("enchantment_provider");
	}

}
