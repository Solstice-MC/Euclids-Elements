package org.solstice.euclidsElements.componentHolder.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.componentHolder.api.AdvancedComponentHolder;
import org.solstice.euclidsElements.componentHolder.core.WorldComponentsState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
public abstract class WorldMixin implements AdvancedComponentHolder {

	@Shadow @Nullable public abstract MinecraftServer getServer();

	@Override
	public ComponentMapImpl getAdvancedComponents() {
		MinecraftServer server = this.getServer();
		if (server == null) return new ComponentMapImpl(ComponentMap.EMPTY);

		WorldComponentsState state = WorldComponentsState.get(this.getServer());
		if (state == null) return new ComponentMapImpl(ComponentMap.EMPTY);

		return state.getComponents((World)(Object)this);
	}

	@Override
	public void setAdvancedComponents(ComponentMapImpl components) {
		MinecraftServer server = this.getServer();
		if (server == null) return;

		WorldComponentsState state = WorldComponentsState.get(this.getServer());
		if (state == null) return;

		state.setComponents((World)(Object)this, components);
	}

}
