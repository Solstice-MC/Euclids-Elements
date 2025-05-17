package org.solstice.euclidsElements.mapTag.api.registry;

import net.minecraft.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.mapTag.api.MapTagKey;

public interface MapTagRegistryEntryHolder<T> {

	@Nullable
	default <R> R getValue(Registry<T> registry, MapTagKey<T, R> mapTag) {
		return null;
	}

}
