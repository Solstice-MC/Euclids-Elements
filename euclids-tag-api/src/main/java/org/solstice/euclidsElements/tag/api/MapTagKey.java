package org.solstice.euclidsElements.tag.api;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.tag.content.mapTag.MapTagManager;

public class MapTagKey<T, R> {

	protected final RegistryKey<Registry<T>> registryReference;
	protected final Codec<R> codec;
	protected final Codec<R> networkCodec;
	protected final Identifier id;

	public RegistryKey<Registry<T>> getRegistryReference() {
		return registryReference;
	}

	public Codec<R> getCodec() {
		return codec;
	}

	public Codec<R> getNetworkCodec() {
		return networkCodec;
	}

	public Identifier getId() {
		return id;
	}

	protected MapTagKey(
		RegistryKey<Registry<T>> registry,
		Codec<R> codec,
		Codec<R> networkCodec,
		Identifier id
	) {
		this.registryReference = registry;
		this.codec = codec;
		this.networkCodec = networkCodec;
		this.id = id;
	}

	public static <T, R> MapTagKey<T, R> of(
		RegistryKey<Registry<T>> registryReference,
		Codec<R> codec,
		Identifier id
	) {
		return MapTagKey.of(registryReference, codec, codec, id);
	}

	public static <T, R> MapTagKey<T, R> of(
		RegistryKey<Registry<T>> registryReference,
		Codec<R> codec,
		Codec<R> networkCodec,
		Identifier id
	) {
		MapTagKey<T, R> key = new MapTagKey<>(registryReference, codec, networkCodec, id);
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
