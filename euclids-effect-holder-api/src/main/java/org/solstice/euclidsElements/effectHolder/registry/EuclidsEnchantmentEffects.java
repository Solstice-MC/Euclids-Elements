package org.solstice.euclidsElements.effectHolder.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.solstice.euclidsElements.EuclidsElements;

public class EuclidsEnchantmentEffects {

	public static void init() {}

	public static final ComponentType<EnchantmentValueEffect> MAX_DURABILITY = register("max_durability", EnchantmentValueEffect.CODEC);

	private static <T> ComponentType<T> register(String name, Codec<T> codec) {
		return register(name, codec, PacketCodecs.registryCodec(codec));
	}

	private static <T> ComponentType<T> register(String name, Codec<T> codec, PacketCodec<RegistryByteBuf, T> packetCodec) {
		ComponentType<T> component = ComponentType.<T>builder()
			.codec(codec)
			.packetCodec(packetCodec)
			.cache()
			.build();
		return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, EuclidsElements.of(name), component);
	}

}
