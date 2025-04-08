package org.solstice.euclidsElements.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.solstice.euclidsElements.EuclidsElements;

public class EuclidsComponents {

    public static final DeferredRegister<DataComponentType<?>> REGISTRY = DeferredRegister
            .create(Registries.DATA_COMPONENT_TYPE, EuclidsElements.MOD_ID);

	public static final DataComponentType<ResourceLocation> CUSTOM_ITEM_MODEL = register("custom_item_model", ResourceLocation.CODEC);

	private static <T> DataComponentType<T> register(String name, Codec<T> codec) {
        return register(name, codec, ByteBufCodecs.fromCodecWithRegistries(codec));
    }

    private static <T> DataComponentType<T> register(String name, Codec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> packetCodec) {
        DataComponentType<T> component = DataComponentType.<T>builder()
			.persistent(codec)
			.networkSynchronized(packetCodec)
			.cacheEncoding()
			.build();
        REGISTRY.register(name, () -> component);
        return component;
    }

}
