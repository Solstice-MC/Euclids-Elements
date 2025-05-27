package org.solstice.euclidsElements.construct.mixin;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import org.solstice.euclidsElements.construct.EuclidsConstructAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

	@Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("RETURN"))
	private void trySpawnConstructs(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
		if (cir.getReturnValue().isAccepted()) EuclidsConstructAPI.trySpawnConstructs(
				context.getWorld(), context.getBlockPos()
		);
	}

}
