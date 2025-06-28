/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.solstice.euclidsElements.tag.content.network.packets;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.tag.content.mapTag.MapTagManager;

import java.util.*;
import java.util.stream.Collectors;

public record KnownRegistryMapTagsPacket(
        Map<RegistryKey<? extends Registry<?>>, List<KnownMapTag>> mapTags
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
                    KnownMapTag.PACKET_CODEC.collect(PacketCodecs.toList())),
            KnownRegistryMapTagsPacket::mapTags,
            KnownRegistryMapTagsPacket::new);

    public record KnownMapTag(Identifier id) {

        public static final PacketCodec<PacketByteBuf, KnownMapTag> PACKET_CODEC = PacketCodec.tuple(
                Identifier.PACKET_CODEC,
                KnownMapTag::id,
                KnownMapTag::new
		);

    }

	@Environment(EnvType.CLIENT)
	public static void handle(final KnownRegistryMapTagsPacket payload, final ClientConfigurationNetworking.Context context) {
		record MandatoryEntry(RegistryKey<? extends Registry<?>> registry, Identifier id) {}
		final Set<MandatoryEntry> ourMandatory = new HashSet<>();
		MapTagManager.getMapTags().forEach((reg, values) -> values.values().forEach(attach -> {
			ourMandatory.add(new MandatoryEntry(reg, attach.getId()));
		}));

		final Set<MandatoryEntry> theirMandatory = new HashSet<>();
		payload.mapTags().forEach((reg, values) -> values.forEach(attach -> {
			theirMandatory.add(new MandatoryEntry(reg, attach.id()));
		}));

		final List<Text> messages = new ArrayList<>();
		final var missingOur = Sets.difference(ourMandatory, theirMandatory);
		if (!missingOur.isEmpty()) {
			messages.add(Text.translatable("neoforge.network.map_tags.missing_our", Text.literal(missingOur.stream()
				.map(e -> e.id() + " (" + e.registry().getValue() + ")")
				.collect(Collectors.joining(", "))).formatted(Formatting.GOLD)));
		}

		final var missingTheir = Sets.difference(theirMandatory, ourMandatory);
		if (!missingTheir.isEmpty()) {
			messages.add(Text.translatable("neoforge.network.map_tags.missing_their", Text.literal(missingTheir.stream()
				.map(e -> e.id() + " (" + e.registry().getValue() + ")")
				.collect(Collectors.joining(", "))).formatted(Formatting.GOLD)));
		}

		if (!messages.isEmpty()) {
			MutableText message = Text.empty();
			final var itr = messages.iterator();
			while (itr.hasNext()) {
				message = message.append(itr.next());
				if (itr.hasNext()) {
					message = message.append("\n");
				}
			}

			context.responseSender().disconnect(message);
			return;
		}

		final var known = new HashMap<RegistryKey<? extends Registry<?>>, Collection<Identifier>>();
		MapTagManager.getMapTags().forEach((key, vals) -> known.put(key, vals.keySet()));
		context.responseSender().sendPacket(new KnownRegistryMapTagsReplyPacket(known));
	}

}
