package org.solstice.euclidsElements.componentHolder.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.item.Item;
import org.solstice.euclidsElements.componentHolder.api.MutableComponentHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public class ItemMixin implements MutableComponentHolder {

	@Shadow @Mutable @Final private ComponentMap components;

	@Override
	public void setComponents(ComponentMapImpl components) {
		this.setImmutableComponents(components);
	}

	@Override
	public ComponentMapImpl getMutableComponents() {
		return new ComponentMapImpl(this.components);
	}

	@Override
	public void setImmutableComponents(ComponentMap components) {
		this.components = components;
	}

}
