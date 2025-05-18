package org.solstice.euclidsElements.mixin;

import com.google.common.collect.Maps;
import net.minecraft.client.sound.NonRepeatingAudioStream;
import net.minecraft.client.sound.OggAudioStream;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.client.sound.StaticSound;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.solstice.euclidsElements.util.OpusAudioStream;
import org.spongepowered.asm.mixin.*;

import java.io.IOException;
import java.io.InputStream;
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
	public CompletableFuture<StaticSound> loadStatic(Identifier path) {
		return this.loadedSounds.computeIfAbsent(path, (id2) -> CompletableFuture.supplyAsync(() -> {
			try {
				NonRepeatingAudioStream stream = this.fromPath(id2);
				ByteBuffer byteBuffer = stream.readAll();
				return new StaticSound(byteBuffer, stream.getFormat());
			} catch (IOException exception) {
				throw new CompletionException(exception);
			}
		}, Util.getDownloadWorkerExecutor()));
	}

	@Unique
	private NonRepeatingAudioStream fromPath(Identifier path) throws IOException {
		String extension = path.getPath().substring(path.getPath().lastIndexOf('.') + 1);
		InputStream inputStream = this.resourceFactory.open(path);
		return switch (extension) {
			case "ogg" -> new OggAudioStream(inputStream);
			case "opus" -> new OpusAudioStream(inputStream);
			default -> throw new IllegalStateException("Unexpected value: " + extension);
		};
	}

}
