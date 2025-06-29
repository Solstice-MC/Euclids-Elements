package org.solstice.euclidsElements.enchantmentTooltips.api.wip;

import net.minecraft.enchantment.effect.EnchantmentLocationBasedEffect;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

public interface TooltipEnchantmentLocationBasedEffect {

	default String getTranslationKey() {
		return Registries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE.getEntry(((EnchantmentLocationBasedEffect)this).getCodec())
			.getKey().orElseThrow().getValue().toTranslationKey("enchantment_location_based_effect");
	}

	default Text getValue(int level) {
		return Text.empty();
	}

	default Text getRequirements() {
		return Text.empty();
	}

	default Text getEffectDescription(int level) {
		String key = this.getTranslationKey();
		Text value = this.getValue(level);
		Text requirements = this.getRequirements();
		return Text.translatable(key, value).append(" ").append(requirements);
	}

}
