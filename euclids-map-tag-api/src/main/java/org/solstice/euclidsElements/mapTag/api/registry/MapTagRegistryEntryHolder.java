package org.solstice.euclidsElements.mapTag.api.registry;

import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.mapTag.api.MapTagKey;

import java.util.Map;

public interface MapTagRegistryEntryHolder<T> {

	default Map<MapTagKey<T, Object>, Object> getMapTags() {
		return Map.of();
	}

	default boolean isIn(MapTagKey<T, ?> mapTag) {
		return this.getMapTags().containsKey(mapTag);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	default <R> R getValue(MapTagKey<T, R> mapTag) {
		return (R) this.getMapTags().getOrDefault(mapTag, null);
	}

	default void addValue(MapTagKey<T, Object> mapTag, Object value) {
		this.getMapTags().put(mapTag, value);
	}

}
