/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.solstice.euclidsElements.tag.content.mapTag;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.tag.api.MapTagKey;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class MapTagManager {

    private static final Map<RegistryKey<Registry<?>>, Map<Identifier, MapTagKey<?, ?>>> MAP_TAGS = new IdentityHashMap<>();

	public static Map<RegistryKey<Registry<?>>, Map<Identifier, MapTagKey<?, ?>>> getMapTags() {
		return MAP_TAGS;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <T> Map<Identifier, MapTagKey<?, ?>> getOrCreate(RegistryKey<Registry<T>> key) {
		return MAP_TAGS.computeIfAbsent((RegistryKey)key, k -> new HashMap<>());
	}

	@SuppressWarnings("unchecked")
    @Nullable
    public static <R> MapTagKey<R, ?> getMapTag(RegistryKey<? extends Registry<R>> registry, Identifier key) {
		Map<Identifier, MapTagKey<?, ?>> map = MAP_TAGS.get(registry);
        return map == null ? null : (MapTagKey<R, ?>) map.get(key);
    }

    public static <T, R> void registerMapTag(MapTagKey<T, R> type) {

		RegistryKey<Registry<T>> registryReference = type.getRegistryReference();

		boolean matchesDynamic = RegistryLoader.DYNAMIC_REGISTRIES.stream()
			.map(RegistryLoader.Entry::key)
			.anyMatch(key -> key.equals(registryReference));

        if (matchesDynamic) {
			boolean matchesSynced = RegistryLoader.SYNCED_REGISTRIES.stream()
				.map(RegistryLoader.Entry::key)
				.anyMatch(key -> key.equals(registryReference));

            if (type.getPacketCodec() != null && matchesSynced) throw new UnsupportedOperationException(
				"Cannot register synced map tag " + type.getId()
				+ " for datapack registry " + registryReference.getValue()
				+ " that is not synced!"
			);
        }

		Map<Identifier, MapTagKey<?, ?>> values = getOrCreate(registryReference);
        if (values.containsKey(type.getId())) {
            throw new IllegalArgumentException(
				"Tried to register map tag type with ID " + type.getId()
				+ " to registry " + registryReference.getValue()
				+ " twice"
			);
        }
        values.put(type.getId(), type);
    }

}
