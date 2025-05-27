package org.solstice.euclidsElements.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.solstice.euclidsElements.EuclidsElements;

public class EuclidsComponentTypes {

	public static void init() {}

	public static final ComponentType<Identifier> CUSTOM_ITEM_MODEL = register("custom_item_model", Identifier.CODEC);
	public static final ComponentType<Identifier> INVENTORY_ITEM_MODEL = register("inventory_item_model", Identifier.CODEC);

	private static <T> ComponentType<T> register(String name, Codec<T> codec) {
        return register(name, codec, PacketCodecs.registryCodec(codec));
    }

    private static <T> ComponentType<T> register(String name, Codec<T> codec, PacketCodec<RegistryByteBuf, T> packetCodec) {
        ComponentType<T> component = ComponentType.<T>builder()
                .codec(codec)
                .packetCodec(packetCodec)
                .cache()
                .build();
		return Registry.register(Registries.DATA_COMPONENT_TYPE, EuclidsElements.of(name), component);
    }

}
