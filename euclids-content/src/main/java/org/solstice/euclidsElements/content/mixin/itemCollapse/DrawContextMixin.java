package org.solstice.euclidsElements.content.mixin.itemCollapse;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.solstice.euclidsElements.content.api.entity.PlayerItemCollapseManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow public abstract void fill(RenderLayer layer, int x1, int y1, int x2, int y2, int color);

	@Inject(
		method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"
		)
	)
	private void renderCollapseProgress(
		CallbackInfo ci,
		@Local(argsOnly = true) ItemStack stack,
		@Local(argsOnly = true, ordinal = 0) int x,
		@Local(argsOnly = true, ordinal = 1) int y
	) {
		ClientPlayerEntity player = this.client.player;
		if (player == null) return;

		float progress = ((PlayerItemCollapseManager)player).getItemCollapseManager().getCollapseProgress(stack.getItem(), this.client.getRenderTickCounter().getTickDelta(true));
		if (progress <= 0) return;

		int y1 = y + MathHelper.floor(16 * progress);
		int y2 = y1 + MathHelper.ceil(16 * (1 - progress));
		this.fill(RenderLayer.getGuiOverlay(), x, y1, x + 16, y2, 0x7fff0000);
	}

}
