/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.solstice.euclidsElements.tag.content.network.packets;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketDecoder;
import net.minecraft.network.codec.PacketEncoder;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.function.TriConsumer;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.content.mapTag.MapTagManager;

import java.util.Map;
import java.util.function.BiFunction;

public record RegistryMapTagSyncPacket<T>(
	RegistryKey<? extends Registry<T>> registryKey,
	Map<Identifier, Map<RegistryKey<T>, ?>> mapTags
) implements CustomPayload {

	public static final Id<RegistryMapTagSyncPacket<?>> ID = new Id<>(
		EuclidsElements.of("registry_map_tag_sync")
	);

	@Override
	public Id<RegistryMapTagSyncPacket<?>> getId() {
		return ID;
	}

	public static final PacketCodec<RegistryByteBuf, RegistryMapTagSyncPacket<?>> PACKET_CODEC = PacketCodec.of(
		RegistryMapTagSyncPacket::encode, RegistryMapTagSyncPacket::decode
	);

	@SuppressWarnings("unchecked")
	public void encode(RegistryByteBuf registryBuf) {
		registryBuf.writeRegistryKey(this.registryKey);
		writeMap(registryBuf, this.mapTags, PacketByteBuf::writeIdentifier, (packetBuf, id, mapTags) -> {
			MapTagKey<T, ?> mapTag = MapTagManager.getMapTag(registryKey, id);
			if (mapTag == null) return;

			PacketCodec<ByteBuf, T> codec = (PacketCodec<ByteBuf, T>) mapTag.getPacketCodec();
			packetBuf.writeMap(mapTags, PacketByteBuf::writeRegistryKey, (buf, value) -> codec.encode(buf, (T) value));
		});
	}

	@SuppressWarnings("unchecked")
	public static <T> RegistryMapTagSyncPacket<T> decode(RegistryByteBuf registryBuf) {
		RegistryKey<Registry<T>> registryReference = (RegistryKey<Registry<T>>) registryBuf.<T>readRegistryRefKey();

		Map<Identifier, Map<RegistryKey<T>, ?>> mapTags = readMap(registryBuf, PacketByteBuf::readIdentifier, (packetBuf, id) -> {
            MapTagKey<T, ?> mapTag = MapTagManager.getMapTag(registryReference, id);
			if (mapTag == null) return Map.of();

			PacketCodec<ByteBuf, T> codec = (PacketCodec<ByteBuf, T>) mapTag.getPacketCodec();
            return packetBuf.readMap(buf -> buf.readRegistryKey(registryReference), codec);
        });
        return new RegistryMapTagSyncPacket<>(registryReference, mapTags);
	}

	public static <K, V> Map<K, V> readMap(
		PacketByteBuf buf,
		PacketDecoder<? super PacketByteBuf, K> keyReader,
		BiFunction<PacketByteBuf, K, V> valueReader
	) {
		int size = buf.readVarInt();
		Map<K, V> map = Maps.newHashMapWithExpectedSize(size);

		for (int i = 0; i < size; ++i) {
			K key = keyReader.decode(buf);
			map.put(key, valueReader.apply(buf, key));
		}

		return map;
	}

	public static <K, V> void writeMap(
		PacketByteBuf buf,
		Map<K, V> map,
		PacketEncoder<? super PacketByteBuf, K> keyWriter,
		TriConsumer<PacketByteBuf, K, V> valueWriter
	) {
		buf.writeVarInt(map.size());
		map.forEach((key, value) -> {
			keyWriter.encode(buf, key);
			valueWriter.accept(buf, key, value);
		});
	}

}
