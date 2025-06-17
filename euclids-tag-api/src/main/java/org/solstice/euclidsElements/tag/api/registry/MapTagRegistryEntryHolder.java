package org.solstice.euclidsElements.tag.api.registry;

import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.tag.api.MapTagKey;

import java.util.Map;

public interface MapTagRegistryEntryHolder<T> {

	default Map<MapTagKey<T, Object>, Object> getMapTags() {
		return Map.of();
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
		return (R) this.getMapTags().getOrDefault(mapTag, defaultValue);
	}

	default void addValue(MapTagKey<T, Object> mapTag, Object value) {
		this.getMapTags().put(mapTag, value);
	}

}
