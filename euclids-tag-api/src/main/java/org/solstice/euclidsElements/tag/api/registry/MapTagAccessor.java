package org.solstice.euclidsElements.tag.api.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.tag.api.MapTagKey;

import java.util.Map;

public interface MapTagAccessor<T> {

	default Map<MapTagKey<T, ?>, Map<RegistryKey<T>, ?>> getMapTags() {
		return Map.of();
	}

	default RegistryKey<Registry<T>> getRegistryKey() {
		return null;
	}

	default boolean isIn(MapTagKey<T, ?> mapTag) {
		return this.getMapTags().containsKey(mapTag);
	}

	@Nullable
	default <R> R getValue(MapTagKey<T, R> mapTag) {
		return this.getValue(mapTag, null);
	}

	@SuppressWarnings("unchecked")
	default <R> R getValue(MapTagKey<T, R> mapTag, R defaultValue) {
		R result = (R) this.getMapTags()
			.getOrDefault(mapTag, Map.of())
			.getOrDefault(this.getRegistryKey(), null);
		if (result == null) return defaultValue;
		return result;
	}

}
