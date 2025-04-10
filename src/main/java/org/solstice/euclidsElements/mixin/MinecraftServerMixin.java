package org.solstice.euclidsElements.mixin;

import net.minecraft.server.MinecraftServer;
import org.solstice.euclidsElements.api.event.ExtraServerLifecycleEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject(method = "<init>", at = @At("RETURN"))
	public void afterInit(CallbackInfo ci) {
		MinecraftServer server = (MinecraftServer)(Object)this;
		ExtraServerLifecycleEvents.AFTER_RESOURCES_LOADED.invoker().afterResourcesReloaded(server);
	}

	@Inject(method = "reloadResources", at = @At("RETURN"))
	private void afterResourceReload(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
		MinecraftServer server = (MinecraftServer)(Object)this;
		ExtraServerLifecycleEvents.AFTER_RESOURCES_LOADED.invoker().afterResourcesReloaded(server);
	}

}
