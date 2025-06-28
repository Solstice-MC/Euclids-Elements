/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.solstice.euclidsElements.tag.content.network.task;


import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.server.network.ServerPlayerConfigurationTask;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.tag.content.mapTag.MapTagManager;
import org.solstice.euclidsElements.tag.content.network.packets.KnownRegistryMapTagsPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public record RegistryMapTagTask(
        ServerConfigurationNetworkHandler listener
) implements ServerPlayerConfigurationTask {

    public static final Identifier ID = EuclidsElements.of("registry_map_tag_task");
    public static final Key KEY = new Key(ID.toString());

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public void sendPacket(Consumer<Packet<?>> task) {
        this.run(customPacketPayload -> task.accept(ServerConfigurationNetworking.createS2CPacket(customPacketPayload)));
    }

	public static void register(ServerConfigurationNetworkHandler handler, MinecraftServer server) {
		handler.addTask(new RegistryMapTagTask(handler));
	}

    public void run(Consumer<CustomPayload> sender) {
        if (!ServerConfigurationNetworking.canSend(listener, KnownRegistryMapTagsPacket.ID)) {
			listener.completeTask(KEY);
			return;
		}

		Map<RegistryKey<? extends Registry<?>>, List<KnownRegistryMapTagsPacket.KnownMapTag>> mapTags = new HashMap<>();
        MapTagManager.getMapTags().forEach((key, values) -> {
			List<KnownRegistryMapTagsPacket.KnownMapTag> list = new ArrayList<>();
            values.forEach((id, mapTag) -> {
                if (mapTag.getNetworkCodec() != null) list.add(new KnownRegistryMapTagsPacket.KnownMapTag(id));
            });
            mapTags.put(key, list);
        });
        sender.accept(new KnownRegistryMapTagsPacket(mapTags));
    }

}
