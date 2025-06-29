package org.solstice.euclidsElements.tag.api.registry;

import net.minecraft.registry.RegistryKey;
import org.solstice.euclidsElements.tag.api.MapTagKey;

import java.util.HashMap;
import java.util.Map;

public interface MapTagHolder<T> {

	default Map<MapTagKey<T, ?>, Map<RegistryKey<T>, ?>> getMapTags() {
		return new HashMap<>();
	}

	default Map<RegistryKey<T>, ?> getMapTag(MapTagKey<T, ?> type) {
		return this.getMapTags().get(type);
	}

}
