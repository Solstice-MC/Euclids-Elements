package org.solstice.euclidsElements.componentHolder.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import org.solstice.euclidsElements.componentHolder.api.AdvancedComponentHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements AdvancedComponentHolder {

	@Shadow @Final ComponentMapImpl components;

	@Shadow public abstract ComponentMap getComponents();

	@Override
	public ComponentMapImpl getAdvancedComponents() {
		return this.components;
	}

	@Override
	public void setAdvancedComponents(ComponentMapImpl newComponents) {
		List<ComponentType<?>> oldComponents = new ArrayList<>(this.components.getTypes());
		oldComponents.forEach(component -> this.components.remove(component));
		this.components.setAll(newComponents);
	}

}
