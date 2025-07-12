package org.solstice.euclidsElements.componentHolder.mixin;

import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.solstice.euclidsElements.componentHolder.api.AdvancedComponentHolder;
import org.solstice.euclidsElements.componentHolder.api.ModifiableComponentHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin implements ModifiableComponentHolder {

	@Shadow @Mutable @Final private ComponentMap components;

	@Override
	public void setComponents(ComponentMap components) {
		this.components = components;
	}

}
