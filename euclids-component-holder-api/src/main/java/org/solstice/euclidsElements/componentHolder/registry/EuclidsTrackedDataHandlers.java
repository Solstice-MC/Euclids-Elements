package org.solstice.euclidsElements.componentHolder.registry;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class EuclidsTrackedDataHandlers {

	public static void init() {
		TrackedDataHandlerRegistry.register(COMPONENT_MAP);
	}

	public static final Codec<ComponentMapImpl> COMPONENT_MAP_CODEC = ComponentMapImpl.CODEC.xmap(
		ComponentMapImpl::new,
		ComponentMapImpl::copy
	);

	public static final TrackedDataHandler<ComponentMapImpl> COMPONENT_MAP = of(COMPONENT_MAP_CODEC);

	public static <T> TrackedDataHandler<T> of(Codec<T> codec) {
		return of(PacketCodecs.unlimitedCodec(codec));
	}

	public static <T> TrackedDataHandler<T> of(PacketCodec<ByteBuf, T> packetCodec) {
		return TrackedDataHandler.create(packetCodec);
	}

}
