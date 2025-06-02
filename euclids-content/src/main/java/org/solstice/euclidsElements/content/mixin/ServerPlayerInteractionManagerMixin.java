package org.solstice.euclidsElements.content.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.solstice.euclidsElements.content.api.item.InteractionPreventingItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

	@ModifyExpressionValue(
			method = "interactBlock",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;shouldCancelInteraction()Z")
	)
	private boolean itemBasedInteractions(boolean shouldRun, @Local(argsOnly = true) ServerPlayerEntity player, @Local(argsOnly = true) Hand hand, @Local(argsOnly = true) BlockHitResult hit) {
		boolean itemInteraction = InteractionPreventingItem.itemBasedInteractions(player, hand, hit);
		if (itemInteraction) return true;
		return shouldRun;
	}

}
