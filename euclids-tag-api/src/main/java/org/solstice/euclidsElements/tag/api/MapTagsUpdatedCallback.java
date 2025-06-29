/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.solstice.euclidsElements.tag.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;

@FunctionalInterface
public interface MapTagsUpdatedCallback {

    Event<MapTagsUpdatedCallback> EVENT = EventFactory.createArrayBacked(MapTagsUpdatedCallback.class,
            callbacks -> (DynamicRegistryManager registryAccess, Registry<?> registry, Cause cause) -> {
                for (MapTagsUpdatedCallback callback : callbacks)
                    callback.onMapTagsUpdated(registryAccess, registry, cause);
            }
    );

    void onMapTagsUpdated(DynamicRegistryManager registryAccess, Registry<?> registry, Cause cause);

    enum Cause {
        CLIENT_SYNC,
        SERVER_RELOAD
    }

}
