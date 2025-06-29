/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.solstice.euclidsElements.tag.content.network.packets;

import com.google.common.collect.Maps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.EuclidsElements;

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

}
