package org.solstice.euclidsElements.componentHolder.registry;

import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class EuclidsTrackedDataHandlers {

	public static void init() {
		TrackedDataHandlerRegistry.register(COMPONENT_MAP);
	}

	public static final PacketCodec<RegistryByteBuf, ComponentChanges> COMPONENT_CHANGES_PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.map(Reference2ObjectArrayMap::new, ComponentType.PACKET_CODEC, PacketCodec.unit(null)),
		changes -> changes.changedComponents,
		ComponentChanges::new
	);

	public static final PacketCodec<RegistryByteBuf, ComponentMapImpl> COMPONENT_MAP_PACKET_CODEC = PacketCodec.tuple(
		COMPONENT_CHANGES_PACKET_CODEC,
		ComponentMapImpl::getChanges,
		EuclidsTrackedDataHandlers::fromMap
	);

	@SuppressWarnings("unchecked")
	public static <T> ComponentMapImpl fromMap(ComponentChanges changes) {
		ComponentMap.Builder builder = ComponentMap.builder();
		changes.changedComponents.forEach((type, component) ->
			component.ifPresent(value -> builder.add((ComponentType<T>)type, (T)value))
		);
		return new ComponentMapImpl(builder.build());
	}

	public static final TrackedDataHandler<ComponentMapImpl> COMPONENT_MAP = of(COMPONENT_MAP_PACKET_CODEC);

	public static <T> TrackedDataHandler<T> of(PacketCodec<RegistryByteBuf, T> packetCodec) {
		return TrackedDataHandler.create(packetCodec);
	}

}
