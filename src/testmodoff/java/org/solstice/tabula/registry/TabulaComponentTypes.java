package org.solstice.tabula.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.solstice.tabula.Tabula;
import org.solstice.tabula.content.HumorValue;

public class TabulaComponentTypes {

	public static void init() {}

	public static final ComponentType<HumorValue> HUMOROUS = register("humorous", HumorValue.CODEC);

	private static <T> ComponentType<T> register(String name, Codec<T> codec) {
		return register(name, codec, PacketCodecs.registryCodec(codec));
	}

	private static <T> ComponentType<T> register(String name, Codec<T> codec, PacketCodec<RegistryByteBuf, T> packetCodec) {
		ComponentType<T> component = ComponentType.<T>builder()
			.codec(codec)
			.packetCodec(packetCodec)
			.cache()
			.build();
		return Registry.register(Registries.DATA_COMPONENT_TYPE, Tabula.of(name), component);
	}

}
