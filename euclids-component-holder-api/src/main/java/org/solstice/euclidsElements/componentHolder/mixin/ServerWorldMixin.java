package org.solstice.euclidsElements.componentHolder.mixin;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.solstice.euclidsElements.componentHolder.api.ComponentPersistentState;
import org.solstice.euclidsElements.componentHolder.api.SerializableComponentHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements SerializableComponentHolder {

	protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
	}

	@Inject(at = @At("TAIL"), method = "<init>")
	private void createAttachmentsPersistentState(CallbackInfo ci) {
		ServerWorld world = (ServerWorld)(Object)this;

		Supplier<ComponentPersistentState> constructor =
			() -> new ComponentPersistentState(world);
		BiFunction<NbtCompound, RegistryWrapper.WrapperLookup, ComponentPersistentState> deserializer =
			(nbt, wrapperLookup) -> ComponentPersistentState.readNbt(world, nbt, wrapperLookup);

		var type = new PersistentState.Type<>(constructor, deserializer, null);
		world.getPersistentStateManager().getOrCreate(type, ComponentPersistentState.ID.toString());
	}

}
