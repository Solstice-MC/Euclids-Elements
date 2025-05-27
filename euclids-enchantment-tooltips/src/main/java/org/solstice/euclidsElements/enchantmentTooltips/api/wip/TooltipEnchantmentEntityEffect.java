package org.solstice.euclidsElements.enchantmentTooltips.api.wip;

import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;

public interface TooltipEnchantmentEntityEffect extends TooltipEnchantmentLocationBasedEffect {

	@Override
	default String getTranslationKey() {
		return Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE.getEntry(((EnchantmentEntityEffect)this).getCodec())
			.getKey().orElseThrow().getValue().toTranslationKey("enchantment_entity_effect");
	}

}
