package org.solstice.euclidsElements.tag.api;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MapTagKey<T, R> {

	private final RegistryKey<Registry<T>> registry;
	private final Codec<R> codec;
	private final Identifier id;

	public RegistryKey<Registry<T>> getRegistry() {
		return registry;
	}

	public Codec<R> getCodec() {
		return codec;
	}

	public Identifier getId() {
		return id;
	}

	protected MapTagKey (
		RegistryKey<Registry<T>> registry,
		Codec<R> codec,
		Identifier id
	) {
		this.registry = registry;
		this.codec = codec;
		this.id = id;
	}

	public static <T, R> MapTagKey<T, R> of(RegistryKey<Registry<T>> registry, Codec<R> codec, Identifier id) {
		MapTagKey<T, R> key = new MapTagKey<>(registry, codec, id);
		MapTagManager.add(key);
		return key;
	}

	public boolean isOf(RegistryKey<? extends Registry<?>> registry) {
		return this.registry == registry;
	}

	@Override
	public String toString() {
		return "MapTagKey[" + this.registry.getValue() + " / " + this.codec.toString() + " / " + this.id + "]";
	}

	public String getTranslationKey() {
		StringBuilder builder = new StringBuilder("map_tag.");

		Identifier registryId = this.registry.getValue();
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
