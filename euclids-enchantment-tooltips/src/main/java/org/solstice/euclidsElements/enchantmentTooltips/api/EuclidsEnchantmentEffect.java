package org.solstice.euclidsElements.enchantmentTooltips.api;

import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.solstice.euclidsElements.enchantmentTooltips.EuclidsEnchantmentTooltips;

import java.util.List;
import java.util.Optional;

public interface EuclidsEnchantmentEffect {

	default <T> String getTranslationKey(Component<T> component) {
		return Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE.getEntry(component.type())
			.getKey().orElseThrow().getValue().toTranslationKey("enchantment_effect");
	}

	default <T> Text getEffectDescription(Component<T> component, int level) {
		String key = this.getTranslationKey(component);
		Text value = this.getValue(level);
		Text requirements = this.getRequirements();
		return Text.translatable(key, value).append(" ").append(requirements);
	}

	default Text getValue(int level) {
		return Text.empty();
	}

	default Text getRequirements() {
		return Text.empty();
	}

	// TODO handle displaying multiple descriptions per effect
	@SuppressWarnings("unchecked")
	static <T> Optional<Text> getDescription(ComponentMap components, Component<T> component, int level) {
		RegistryEntry<ComponentType<?>> entry = Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE.getEntry(component.type());
		if (entry.isIn(EuclidsEnchantmentTooltips.IGNORE_TOOLTIPS)) return Optional.empty();

		String key = entry.getKey().orElseThrow().getValue().toTranslationKey("enchantment_effect");


		T effect = components.get(component.type());
		if (effect instanceof List<?> effects) effect = (T) effects.getFirst();

		Text result;
		if (effect instanceof EuclidsEnchantmentEffect tooltipEffect) {
			result = tooltipEffect.getEffectDescription(component, level);
		} else {
			result = Text.translatable(key);
		}

		return Optional.of(Text.literal("◇ ").append(result).formatted(Formatting.GRAY));
	}

}
