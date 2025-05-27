package org.solstice.euclidsElements.enchantmentTooltips;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.solstice.euclidsElements.EuclidsElements;

public class EuclidsEnchantmentTooltips {

	public static final TagKey<ComponentType<?>> IGNORE_TOOLTIPS =
		TagKey.of(RegistryKeys.ENCHANTMENT_EFFECT_COMPONENT_TYPE, EuclidsElements.of("ignore_tooltips"));

}
