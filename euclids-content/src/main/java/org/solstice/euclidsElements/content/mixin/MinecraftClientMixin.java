package org.solstice.euclidsElements.content.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.solstice.euclidsElements.content.api.item.GenericAttackingItem;
import org.solstice.euclidsElements.content.api.packet.GenericAttackPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow @Nullable public ClientPlayerEntity player;

	@Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"))
	private void genericAttackHandler(CallbackInfoReturnable<Boolean> cir) {
		if (player == null) return;
		ItemStack stack = player.getMainHandStack();
		if (stack.getItem() instanceof GenericAttackingItem genericAttacking) {
			ClientPlayNetworking.send(new GenericAttackPacket(stack));
			genericAttacking.genericAttack(player.getWorld(), player, stack);
		}
	}

}
