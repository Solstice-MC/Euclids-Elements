/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.solstice.euclidsElements.tag.content.network.packets;

import com.google.common.collect.Maps;
import io.netty.util.AttributeKey;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.tag.content.network.task.RegistryMapTagTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public record KnownRegistryMapTagsReplyPacket (
        Map<RegistryKey<? extends Registry<?>>, Collection<Identifier>> mapTags
) implements CustomPayload {

    public static final Id<KnownRegistryMapTagsReplyPacket> ID = new Id<>(
        EuclidsElements.of("known_registry_map_tags_reply")
    );

	public static final AttributeKey<Map<RegistryKey<? extends Registry<?>>, Collection<Identifier>>> KNOWN_MAP_TAGS =
		AttributeKey.valueOf(EuclidsElements.of("known_map_tags").toString()
	);

    @Override
    public Id<KnownRegistryMapTagsReplyPacket> getId() {
        return ID;
    }

    public static final PacketCodec<PacketByteBuf, KnownRegistryMapTagsReplyPacket> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.map(
                    Maps::newHashMapWithExpectedSize,
                    Identifier.PACKET_CODEC.xmap(RegistryKey::ofRegistry, RegistryKey::getValue),
                    Identifier.PACKET_CODEC.collect(PacketCodecs.toCollection(ArrayList::new))),
            KnownRegistryMapTagsReplyPacket::mapTags,
            KnownRegistryMapTagsReplyPacket::new
	);

	public static void handle(final KnownRegistryMapTagsReplyPacket payload, final ServerConfigurationNetworking.Context context) {
		context.networkHandler().connection.channel.pipeline().lastContext().channel().attr(KNOWN_MAP_TAGS).set(payload.mapTags());
		context.networkHandler().completeTask(RegistryMapTagTask.KEY);
	}

}
