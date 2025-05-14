package org.solstice.euclidsElements.api.mapTag;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public record MapTagKey<T, R> (
	RegistryKey<Registry<T>> registry,
	Codec<R> codec,
	Identifier id
) {

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
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("map_tag.");
		Identifier registryId = this.registry().getValue();
		Identifier tagId = this.id();
		if (!registryId.getNamespace().equals("minecraft")) {
			stringBuilder.append(registryId.getNamespace()).append(".");
		}

		stringBuilder.append(registryId.getPath().replace("/", ".")).append(".").append(tagId.getNamespace()).append(".").append(tagId.getPath().replace("/", ".").replace(":", "."));
		return stringBuilder.toString();
	}

	public Text getName() {
		return Text.translatable(this.getTranslationKey());
	}

}
