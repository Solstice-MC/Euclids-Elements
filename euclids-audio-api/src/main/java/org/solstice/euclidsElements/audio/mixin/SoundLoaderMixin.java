package org.solstice.euclidsElements.audio.mixin;

import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import net.minecraft.client.sound.*;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.solstice.euclidsElements.audio.api.SoundTypeManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Mixin(SoundLoader.class)
public class SoundLoaderMixin {

	@Shadow @Final private ResourceFactory resourceFactory;
	@Shadow @Final private Map<Identifier, CompletableFuture<StaticSound>> loadedSounds = Maps.newHashMap();

	@Inject(method = "loadStatic(Lnet/minecraft/util/Identifier;)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), cancellable = true)
	public void loadStatic(Identifier truePath, CallbackInfoReturnable<CompletableFuture<StaticSound>> cir) {
		CompletableFuture<StaticSound> result = this.loadedSounds.computeIfAbsent(truePath, path -> CompletableFuture.supplyAsync(() -> {
			try {
				NonRepeatingAudioStream stream = SoundTypeManager.staticStream(this.resourceFactory, path);
				ByteBuffer byteBuffer = stream.readAll();
				return new StaticSound(byteBuffer, stream.getFormat());
			} catch (IOException exception) {
				throw new CompletionException(exception);
			}
		}, Util.getDownloadWorkerExecutor()));
		cir.setReturnValue(result);
	}

	@Inject(method = "loadStreamed", at = @At("HEAD"), cancellable = true)
	public void loadStreamed(Identifier path, boolean looping, CallbackInfoReturnable<CompletableFuture<AudioStream>> cir) {
		CompletableFuture<AudioStream> result = CompletableFuture.supplyAsync(() -> {
			try {
				if (looping) return SoundTypeManager.repeatingStream(this.resourceFactory, path);
				return SoundTypeManager.staticStream(this.resourceFactory, path);
			} catch (IOException exception) {
				throw new CompletionException(exception);
			}
		}, Util.getDownloadWorkerExecutor());
		cir.setReturnValue(result);
	}

}
