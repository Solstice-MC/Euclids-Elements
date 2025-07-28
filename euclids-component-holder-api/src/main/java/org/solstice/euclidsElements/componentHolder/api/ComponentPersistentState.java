package org.solstice.euclidsElements.componentHolder.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import org.solstice.euclidsElements.EuclidsElements;

public class ComponentPersistentState extends PersistentState {

	public static final Identifier ID = EuclidsElements.of("components");

	protected final SerializableComponentHolder holder;

	public ComponentPersistentState(SerializableComponentHolder holder) {
		this.holder = holder;
	}

	public static ComponentPersistentState readNbt(SerializableComponentHolder holder, NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
		holder.readComponentsFromNbt(nbt, wrapperLookup);
		return new ComponentPersistentState(holder);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		this.holder.writeComponentsToNbt(nbt, registryLookup);
		return nbt;
	}

}
