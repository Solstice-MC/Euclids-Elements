package org.solstice.euclidsElements.componentHolder.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.world.World;
import org.solstice.euclidsElements.componentHolder.api.SerializableComponentHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(World.class)
public abstract class WorldMixin implements SerializableComponentHolder {

//	@Shadow @Nullable public abstract MinecraftServer getServer();

	@Unique ComponentMapImpl components = new ComponentMapImpl(ComponentMap.EMPTY);

	@Override
	public ComponentMapImpl getMutableComponents() {
		return this.components;
	}

	@Override
	public void setComponents(ComponentMapImpl components) {
		this.components = components;
	}

//	@Override
//	public ComponentMapImpl getAdvancedComponents() {
//		MinecraftServer server = this.getServer();
//		if (server == null) return new ComponentMapImpl(ComponentMap.EMPTY);
//
//		WorldComponentsState state = WorldComponentsState.get(this.getServer());
//		if (state == null) return new ComponentMapImpl(ComponentMap.EMPTY);
//
//		return state.getComponents((World)(Object)this);
//	}
//
//	@Override
//	public void setAdvancedComponents(ComponentMapImpl components) {
//		MinecraftServer server = this.getServer();
//		if (server == null) return;
//
//		WorldComponentsState state = WorldComponentsState.get(this.getServer());
//		if (state == null) return;
//
//		state.setComponents((World)(Object)this, components);
//	}

//	@Inject(at = @At("TAIL"), method = "<init>")
//	private void createAttachmentsPersistentState(CallbackInfo ci) {
//		World world = (World)(Object)this;
//
//		Supplier<ComponentPersistentState> constructor =
//			() -> new ComponentPersistentState(world);
//		BiFunction<NbtCompound, RegistryWrapper.WrapperLookup, ComponentPersistentState> deserializer =
//			(nbt, wrapperLookup) -> ComponentPersistentState.readNbt(world, nbt, wrapperLookup);
//
//		var type = new PersistentState.Type<>(constructor, deserializer, null);
//		if (world instanceof ServerWorld serverWorld)
//			serverWorld.getPersistentStateManager().getOrCreate(type, ComponentPersistentState.ID.toString());
//	}

}
