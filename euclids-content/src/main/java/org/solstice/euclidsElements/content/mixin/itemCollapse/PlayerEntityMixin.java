package org.solstice.euclidsElements.content.mixin.itemCollapse;

import net.minecraft.entity.player.PlayerEntity;
import org.solstice.euclidsElements.content.api.ItemCollapseManager;
import org.solstice.euclidsElements.content.api.entity.PlayerItemCollapseManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerItemCollapseManager {

	@Unique private ItemCollapseManager itemCollapseManager;

	@Override
	public ItemCollapseManager getItemCollapseManager() {
		return this.itemCollapseManager;
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onConstruct(CallbackInfo ci) {
		this.itemCollapseManager = new ItemCollapseManager((PlayerEntity)(Object)this);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		this.getItemCollapseManager().update();
	}

}
