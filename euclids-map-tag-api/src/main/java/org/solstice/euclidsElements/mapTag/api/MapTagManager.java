package org.solstice.euclidsElements.mapTag.api;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class MapTagManager {

	private static final Map<RegistryKey<? extends Registry<?>>, Map<Identifier, MapTagKey<?, ?>>> DATA_MAPS = new IdentityHashMap<>();

	private static <T, R> Map<Identifier, MapTagKey<?, ?>> getOrCreate(RegistryKey<? extends Registry<T>> key) {
		return DATA_MAPS.computeIfAbsent(key, k -> new HashMap<>());
	}

	public static <T, R> void add(MapTagKey<T, R> type) {
		Map<Identifier, MapTagKey<?, ?>> map = getOrCreate(type.getRegistry());
		map.put(type.getId(), type);
	}

	@SuppressWarnings("unchecked")
	public static <T, R> MapTagKey<T, R> get(RegistryKey<?> registry, Identifier key) {
		Map<Identifier, MapTagKey<?, ?>> map = DATA_MAPS.getOrDefault(registry, null);
		if (map == null) return null;
		return (MapTagKey<T, R>) map.get(key);
	}

}
