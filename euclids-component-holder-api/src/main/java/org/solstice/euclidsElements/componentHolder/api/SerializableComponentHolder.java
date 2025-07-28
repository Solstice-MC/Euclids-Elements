package org.solstice.euclidsElements.componentHolder.api;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.EuclidsElements;

import java.util.function.Consumer;

public interface SerializableComponentHolder extends MutableComponentHolder {

	Identifier ID = EuclidsElements.of("components");
	Codec<ComponentMap> CODEC = ComponentMap.CODEC.optionalFieldOf(ID.toString(), ComponentMap.EMPTY).codec();

	default void writeComponentsToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
		RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, wrapperLookup);
		Consumer<NbtElement> consumer = newNbt -> nbt.put(ID.toString(), newNbt);
		CODEC.encodeStart(ops, this.getComponents())
			.resultOrPartial(error -> EuclidsElements.LOGGER.warn("Failed to save entity components: {}", error))
			.ifPresent(consumer);
	}

	default void readComponentsFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
		RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, wrapperLookup);
		NbtCompound components = nbt.getCompound(ID.toString());
		CODEC.parse(ops, components)
			.resultOrPartial(error -> EuclidsElements.LOGGER.warn("Failed to load entity components: {}", error))
			.ifPresent(this::setImmutableComponents);
	}

	@Nullable
	ServerWorld getServerWorld();

	@Override
	default ComponentMapImpl getMutableComponents() {
		ServerWorld server = this.getServerWorld();
		if (server == null) return new ComponentMapImpl(ComponentMap.EMPTY);

		ComponentPersistentState state = new ComponentPersistentState(server);

//		return state.getComponents((World)(Object)this);
		return null;
	}

	@Override
	default void setComponents(ComponentMapImpl components) {
		ServerWorld server = this.getServerWorld();
		if (server == null) return;

		ComponentPersistentState state = new ComponentPersistentState(server);

//		state.setComponents((World)(Object)this, components);
	}

}
