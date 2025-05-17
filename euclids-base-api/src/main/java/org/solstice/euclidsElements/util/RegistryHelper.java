package org.solstice.euclidsElements.util;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;

import java.util.Optional;

public class RegistryHelper {

	@SuppressWarnings("unchecked")
	public static <T> RegistryEntryList<T> getTagValues (
		RegistryWrapper.WrapperLookup lookup,
		RegistryKey<Registry<T>> registry,
		TagKey<T> tag
	) {
		if (lookup != null) {
			Optional<RegistryEntryList.Named<T>> result = lookup.getWrapperOrThrow(registry).getOptional(tag);
			if (result.isPresent()) return result.get();
		}

		return RegistryEntryList.of(new RegistryEntry[0]);
	}

}
