package org.solstice.euclidsElements.api.mapTag.registry;

import net.minecraft.registry.RegistryKey;
import org.solstice.euclidsElements.api.mapTag.MapTagKey;

import java.util.Map;

public interface MapTagRegistryHolder<T> {

	default Map<MapTagKey<T, ?>, Map<RegistryKey<T>, ?>> getMapTags() {
		return Map.of();
	}

	default <R> R getMapTagValue(MapTagKey<T, R> type, RegistryKey<T> key) {
		return this.getMapTag(type).getOrDefault(key, null);
	}

	@SuppressWarnings("unchecked")
	default <R> Map<RegistryKey<T>, R> getMapTag(MapTagKey<T, R> type) {
		return (Map<RegistryKey<T>, R>) this.getMapTags().getOrDefault(type, Map.of());
	}

}
