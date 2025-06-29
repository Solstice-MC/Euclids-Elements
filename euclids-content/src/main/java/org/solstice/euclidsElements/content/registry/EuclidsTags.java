package org.solstice.euclidsElements.content.registry;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.solstice.euclidsElements.EuclidsElements;

public class EuclidsTags {

	public static void init() {}

	public static final TagKey<ComponentType<?>> TOOLTIP_HOLDER = of("tooltip_holder", RegistryKeys.DATA_COMPONENT_TYPE);

	protected static <T> TagKey<T> of(String name, RegistryKey<Registry<T>> key) {
		return TagKey.of(key, EuclidsElements.of(name));
	}

}
