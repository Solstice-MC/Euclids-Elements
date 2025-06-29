package org.solstice.euclidsElements.content.mixin.stackSize;

import net.minecraft.server.command.ItemCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemCommand.class)
public abstract class ItemCommandMixin {

	@ModifyConstant( method = "register", constant = @Constant(intValue = 99))
	private static int changeMaxStackSizeLimit(int original) {
		return 1024;
	}

}
