package org.solstice.euclidsElements.content.test;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registry;

public class ComponentRegistryContainer extends RegistryContainer<ComponentType<?>> {

	public ComponentRegistryContainer(String modId, Registry<ComponentType<?>> registry) {
		super(modId, registry);
	}

	public <T> ComponentType<T> register(String name, Codec<T> codec) {
		return register(name, codec, PacketCodecs.unlimitedRegistryCodec(codec));
	}

	@SuppressWarnings("unchecked")
	public <T> ComponentType<T> register(String name, Codec<T> codec, PacketCodec<RegistryByteBuf, T> packetCodec) {
		ComponentType<?> component = ComponentType.<T>builder()
			.codec(codec)
			.packetCodec(packetCodec)
			.cache()
			.build();
		return (ComponentType<T>) this.register(name, component);
	}

}
