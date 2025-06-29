/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.solstice.euclidsElements.tag.content.network.packets;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.tag.content.mapTag.MapTagManager;

import java.util.*;

public record KnownRegistryMapTagsPacket(
	Map<RegistryKey<? extends Registry<?>>, List<Identifier>> mapTags
) implements CustomPayload {

    public static final Id<KnownRegistryMapTagsPacket> ID = new Id<>(
		EuclidsElements.of("known_registry_map_tags")
    );

    @Override
    public Id<KnownRegistryMapTagsPacket> getId() {
        return ID;
    }

    public static final PacketCodec<PacketByteBuf, KnownRegistryMapTagsPacket> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.map(
			Maps::newHashMapWithExpectedSize,
			Identifier.PACKET_CODEC.xmap(RegistryKey::ofRegistry, RegistryKey::getValue),
			Identifier.PACKET_CODEC.collect(PacketCodecs.toList())),
		KnownRegistryMapTagsPacket::mapTags,
		KnownRegistryMapTagsPacket::new
	);

	@Environment(EnvType.CLIENT)
	public static void handle(KnownRegistryMapTagsPacket p, ClientConfigurationNetworking.Context context) {
		Map<RegistryKey<? extends Registry<?>>, Collection<Identifier>> known = new HashMap<>();
		MapTagManager.getMapTags().forEach((key, vals) ->
			known.put(key, vals.keySet())
		);

		context.responseSender().sendPacket(new KnownRegistryMapTagsReplyPacket(known));
	}

}
