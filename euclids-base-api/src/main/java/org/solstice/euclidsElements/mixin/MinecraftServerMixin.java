package org.solstice.euclidsElements.mixin;

import net.minecraft.server.MinecraftServer;
import org.solstice.euclidsElements.api.event.EuclidsServerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

	@Inject(method = "<init>", at = @At("RETURN"))
	public void afterInit(CallbackInfo ci) {
		MinecraftServer server = (MinecraftServer)(Object)this;
		EuclidsServerEvents.AFTER_RESOURCES_LOADED.invoker().afterResourcesReloaded(server);
	}

}
