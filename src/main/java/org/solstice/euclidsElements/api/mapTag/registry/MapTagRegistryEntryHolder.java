package org.solstice.euclidsElements.api.mapTag.registry;

import net.minecraft.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.api.mapTag.MapTagKey;

public interface MapTagRegistryEntryHolder<T> {

	@Nullable
	default <R> R getValue(Registry<T> registry, MapTagKey<T, R> mapTag) {
		return null;
	}

}
