package org.solstice.euclidsElements.componentHolder.registry;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.codec.PacketCodecs;

public class EuclidsTrackedDataHandlers {

	public static final Codec<ComponentMapImpl> IMPLEMENTATION_CODEC = ComponentMapImpl.CODEC.xmap(
		ComponentMapImpl::new,
		ComponentMapImpl::copy
	);

	public static void init() {}

	public static final TrackedDataHandler<ComponentMapImpl> COMPONENT_MAP = register(IMPLEMENTATION_CODEC);

	public static <T> TrackedDataHandler<T> register(Codec<T> codec) {
		TrackedDataHandler<T> handler = TrackedDataHandler.create(PacketCodecs.codec(codec));
		TrackedDataHandlerRegistry.register(handler);
		return handler;
	}

}
