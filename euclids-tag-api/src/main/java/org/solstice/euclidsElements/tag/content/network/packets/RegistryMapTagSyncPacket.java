/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.solstice.euclidsElements.tag.content.network.packets;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
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
import net.minecraft.util.JsonHelper;
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
    private static final Gson GSON = new Gson();

	@Environment(EnvType.CLIENT)
    @Override
    public Id<RegistryMapTagSyncPacket<?>> getId() {
        return ID;
    }

	@Environment(EnvType.CLIENT)
    public static final PacketCodec<RegistryByteBuf, RegistryMapTagSyncPacket<?>> PACKET_CODEC = PacketCodec.of(
            RegistryMapTagSyncPacket::write, RegistryMapTagSyncPacket::decode);

	@SuppressWarnings({"unchecked", "rawtypes"})
	public void write(RegistryByteBuf buf) {
		buf.writeRegistryKey(registryKey);
		writeMap(buf, mapTags, PacketByteBuf::writeIdentifier, (packetBuf, key, attach) -> {
			final MapTagKey<T, ?> mapTag = MapTagManager.getMapTag(registryKey, key);
			if (mapTag == null) return;
			packetBuf.writeMap(attach, PacketByteBuf::writeRegistryKey, (registryBuf, value) -> writeJsonWithRegistryCodec(
				(RegistryByteBuf) registryBuf,
				(Codec) mapTag.getNetworkCodec(),
				value
			));
		});
	}

	@SuppressWarnings("unchecked")
    public static <T> RegistryMapTagSyncPacket<T> decode(RegistryByteBuf buf) {
		// noinspection RedundantCast
        final RegistryKey<Registry<T>> registryKey = (RegistryKey<Registry<T>>) (Object) buf.readRegistryRefKey();

		final Map<Identifier, Map<RegistryKey<T>, ?>> attach = readMap(buf, PacketByteBuf::readIdentifier, (b1, key) -> {
            final MapTagKey<T, ?> mapTag = MapTagManager.getMapTag(registryKey, key);
            return b1.readMap(bf -> bf.readRegistryKey(registryKey), bf -> readJsonWithRegistryCodec((RegistryByteBuf) bf, mapTag.getNetworkCodec()));
        });
        return new RegistryMapTagSyncPacket<>(registryKey, attach);
    }

    private static <T> T readJsonWithRegistryCodec(RegistryByteBuf buf, Codec<T> codec) {
        JsonElement jsonelement = JsonHelper.deserialize(GSON, buf.readString(), JsonElement.class);
        DataResult<T> dataresult = codec.parse(buf.getRegistryManager().getOps(JsonOps.INSTANCE), jsonelement);
        return dataresult.getOrThrow(name -> new DecoderException("Failed to decode json: " + name));
    }

    private static <T> void writeJsonWithRegistryCodec(RegistryByteBuf buf, Codec<T> codec, T value) {
        DataResult<JsonElement> dataresult = codec.encodeStart(buf.getRegistryManager().getOps(JsonOps.INSTANCE), value);
        buf.writeString(GSON.toJson(dataresult.getOrThrow(message -> new EncoderException("Failed to encode: " + message + " " + value))));
    }

	@Environment(EnvType.CLIENT)
    public static <K, V> Map<K, V> readMap(PacketByteBuf friendlyByteBuf, PacketDecoder<? super PacketByteBuf, K> keyReader, BiFunction<PacketByteBuf, K, V> valueReader) {
        final int size = friendlyByteBuf.readVarInt();
        final Map<K, V> map = Maps.newHashMapWithExpectedSize(size);

        for (int i = 0; i < size; ++i) {
            final K k = keyReader.decode(friendlyByteBuf);
            map.put(k, valueReader.apply(friendlyByteBuf, k));
        }

        return map;
    }

    public static <K, V> void writeMap(PacketByteBuf friendlyByteBuf, Map<K, V> map, PacketEncoder<? super PacketByteBuf, K> keyWriter, TriConsumer<PacketByteBuf, K, V> valueWriter) {
        friendlyByteBuf.writeVarInt(map.size());
        map.forEach((key, value) -> {
            keyWriter.encode(friendlyByteBuf, key);
            valueWriter.accept(friendlyByteBuf, key, value);
        });
    }

	@Environment(EnvType.CLIENT)
	public static <R> void handle(final RegistryMapTagSyncPacket<R> payload, final ClientPlayNetworking.Context context) {
		context.client().submit(() -> {
			try {
				World world = MinecraftClient.getInstance().world;
				if (world == null) return;

				DynamicRegistryManager registryManager = world.getRegistryManager();
				final SimpleRegistry<R> registry = (SimpleRegistry<R>) registryManager.get(payload.registryKey());
				registry.getMapTags().clear();
				payload.mapTags.forEach((id, maps) -> {
					MapTagKey<R, ?> mapTag = MapTagManager.getMapTag(payload.registryKey(), id);
					var values = Collections.unmodifiableMap(maps);
					registry.getMapTags().put(mapTag, values);
				});
				System.out.println(registry.getMapTags());
				MapTagsUpdatedCallback.EVENT.invoker().onMapTagsUpdated(registryManager, registry, MapTagsUpdatedCallback.Cause.CLIENT_SYNC);
			} catch (Exception exception) {
				EuclidsElements.LOGGER.error("Failed to handle registry data map sync: ", exception);
//				context.responseSender().disconnect(Text.literal("neoforge.network.map_tags.failed", payload.registryKey().getValue().toString(), exception.toString()));
				context.responseSender().disconnect(Text.literal("Failed to handle registry data map sync"));
			}
		});
	}

}
