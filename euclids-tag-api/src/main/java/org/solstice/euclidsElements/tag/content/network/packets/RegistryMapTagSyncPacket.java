/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.solstice.euclidsElements.tag.content.network.packets;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketDecoder;
import net.minecraft.network.codec.PacketEncoder;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.commons.lang3.function.TriConsumer;
import org.solstice.euclidsElements.EuclidsElements;
import org.solstice.euclidsElements.tag.api.MapTagsUpdatedCallback;
import org.solstice.euclidsElements.tag.api.MapTagKey;
import org.solstice.euclidsElements.tag.content.mapTag.MapTagManager;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;

public record RegistryMapTagSyncPacket<T> (
        RegistryKey<? extends Registry<T>> registryKey,
        Map<Identifier, Map<RegistryKey<T>, ?>> mapTags
) implements CustomPayload {

    public static final Id<RegistryMapTagSyncPacket<?>> ID = new Id<>(
        EuclidsElements.of("registry_map_tag_sync")
    );

	@Environment(EnvType.CLIENT)
    @Override
    public Id<RegistryMapTagSyncPacket<?>> getId() {
        return ID;
    }

	@Environment(EnvType.CLIENT)
    public static final PacketCodec<RegistryByteBuf, RegistryMapTagSyncPacket<?>> PACKET_CODEC = PacketCodec.of(
            RegistryMapTagSyncPacket::encode, RegistryMapTagSyncPacket::decode
	);

	@Environment(EnvType.CLIENT)
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

	@Environment(EnvType.CLIENT)
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

	@Environment(EnvType.CLIENT)
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

	@Environment(EnvType.CLIENT)
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

	@Environment(EnvType.CLIENT)
	public static <R> void handle(final RegistryMapTagSyncPacket<R> payload, final ClientPlayNetworking.Context context) {
		context.client().submit(() -> {
			try {
				World world = MinecraftClient.getInstance().world;
				if (world == null) return;

				DynamicRegistryManager registryManager = world.getRegistryManager();
				SimpleRegistry<R> registry = (SimpleRegistry<R>) registryManager.get(payload.registryKey());
				registry.getMapTags().clear();
				payload.mapTags.forEach((id, maps) -> {
					MapTagKey<R, ?> mapTag = MapTagManager.getMapTag(payload.registryKey(), id);
					Map<RegistryKey<R>, ?> values = Collections.unmodifiableMap(maps);
					registry.getMapTags().put(mapTag, values);
				});
				MapTagsUpdatedCallback.EVENT.invoker().onMapTagsUpdated(registryManager, registry, MapTagsUpdatedCallback.Cause.CLIENT_SYNC);
			} catch (Exception exception) {
				EuclidsElements.LOGGER.error("Failed to handle registry map tag sync: ", exception);
				context.responseSender().disconnect(Text.literal("Failed to handle registry map tag sync"));
			}
		});
	}

}
