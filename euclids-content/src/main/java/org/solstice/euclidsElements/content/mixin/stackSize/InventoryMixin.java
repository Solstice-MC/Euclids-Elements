package org.solstice.euclidsElements.content.mixin.stackSize;

import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Inventory.class)
public interface InventoryMixin {

	@ModifyConstant( method = "getMaxCountPerStack", constant = @Constant(intValue = 99))
	private int changeMaxStackSizeLimit(int original) {
		return 1024;
	}

}
