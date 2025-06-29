package org.solstice.euclidsElements.tag.api;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.tag.content.mapTag.MapTagManager;

public class MapTagKey<T, R> {

	protected final RegistryKey<Registry<T>> registryReference;
	protected final Codec<R> codec;
	protected final PacketCodec<ByteBuf, R> packetCodec;
	protected final Identifier id;

	public RegistryKey<Registry<T>> getRegistryReference() {
		return registryReference;
	}

	public Codec<R> getCodec() {
		return codec;
	}

	public PacketCodec<ByteBuf, R> getPacketCodec() {
		return packetCodec;
	}

	public Identifier getId() {
		return id;
	}

	protected MapTagKey(
		RegistryKey<Registry<T>> registry,
		Codec<R> codec,
		PacketCodec<ByteBuf, R> packetCodec,
		Identifier id
	) {
		this.registryReference = registry;
		this.codec = codec;
		this.packetCodec = packetCodec;
		this.id = id;
	}

	public static <T, R> MapTagKey<T, R> of(
		RegistryKey<Registry<T>> registryReference,
		Codec<R> codec,
		Identifier id
	) {
		return MapTagKey.of(registryReference, codec, PacketCodecs.unlimitedCodec(codec), id);
	}

	public static <T, R> MapTagKey<T, R> of(
		RegistryKey<Registry<T>> registryReference,
		Codec<R> codec,
		PacketCodec<ByteBuf, R> packetCodec,
		Identifier id
	) {
		MapTagKey<T, R> key = new MapTagKey<>(registryReference, codec, packetCodec, id);
		MapTagManager.registerMapTag(key);
		return key;
	}

	public boolean isOf(RegistryKey<? extends Registry<?>> registry) {
		return this.registryReference == registry;
	}

	@Override
	public String toString() {
		return "MapTagKey[" + this.registryReference.getValue() + " / " + this.codec.toString() + " / " + this.id + "]";
	}

	public String getTranslationKey() {
		StringBuilder builder = new StringBuilder("map_tag.");

		Identifier registryId = this.registryReference.getValue();
		if (!registryId.getNamespace().equals("minecraft"))
			builder.append(registryId.getNamespace()).append(".");

		return builder
			.append(registryId.getPath().replace("/", "."))
			.append(".")
			.append(this.id.getNamespace())
			.append(".")
			.append(this.id.getPath().replace("/", ".").replace(":", "."))
			.toString();
	}

	public Text getName() {
		return Text.translatable(this.getTranslationKey());
	}

}
