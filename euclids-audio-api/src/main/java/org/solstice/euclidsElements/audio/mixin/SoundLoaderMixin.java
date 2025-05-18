package org.solstice.euclidsElements.audio.mixin;

import com.google.common.collect.Maps;
import net.minecraft.client.sound.*;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.solstice.euclidsElements.audio.api.SoundTypeManager;
import org.spongepowered.asm.mixin.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Mixin(SoundLoader.class)
public class SoundLoaderMixin {

	@Shadow @Final private ResourceFactory resourceFactory;
	@Shadow @Final private final Map<Identifier, CompletableFuture<StaticSound>> loadedSounds = Maps.newHashMap();

	/**
	 * @author Solstice
	 * @reason Multiple audio format support
	 */
	@Overwrite
	public CompletableFuture<StaticSound> loadStatic(Identifier truePath) {
		return this.loadedSounds.computeIfAbsent(truePath, path -> CompletableFuture.supplyAsync(() -> {
			try {
				NonRepeatingAudioStream stream = SoundTypeManager.staticStream(this.resourceFactory, path);
				ByteBuffer byteBuffer = stream.readAll();
				return new StaticSound(byteBuffer, stream.getFormat());
			} catch (IOException exception) {
				throw new CompletionException(exception);
			}
		}, Util.getDownloadWorkerExecutor()));
	}

	/**
	 * @author Solstice
	 * @reason Multiple audio format support
	 */
	@Overwrite
	public CompletableFuture<AudioStream> loadStreamed(Identifier path, boolean repeatInstantly) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				if (repeatInstantly) return SoundTypeManager.repeatingStream(this.resourceFactory, path);
				return SoundTypeManager.staticStream(this.resourceFactory, path);
			} catch (IOException iOException) {
				throw new CompletionException(iOException);
			}
		}, Util.getDownloadWorkerExecutor());
	}

}
