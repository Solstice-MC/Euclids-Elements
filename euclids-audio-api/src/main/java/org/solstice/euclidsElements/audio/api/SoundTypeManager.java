package org.solstice.euclidsElements.audio.api;

import net.minecraft.client.sound.NonRepeatingAudioStream;
import net.minecraft.client.sound.OggAudioStream;
import net.minecraft.client.sound.RepeatingAudioStream;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;

public class SoundTypeManager {

	public static NonRepeatingAudioStream staticStream(ResourceFactory resourceFactory, Identifier path) throws IOException {
		String extension = path.getPath().substring(path.getPath().lastIndexOf('.') + 1);
		InputStream stream = resourceFactory.open(path);
		var test = switch (extension) {
			case "ogg" -> new OggAudioStream(stream);
			case "opus" -> new OpusAudioStream(stream);
			default -> throw new IllegalStateException("Unexpected audio file extension: " + extension);
		};
		return test;
	}

		public static RepeatingAudioStream repeatingStream(ResourceFactory resourceFactory, Identifier path) throws IOException {
		String extension = path.getPath().substring(path.getPath().lastIndexOf('.') + 1);
		InputStream stream = resourceFactory.open(path);
		RepeatingAudioStream.DelegateFactory delegateFactory = switch (extension) {
			case "ogg" -> OggAudioStream::new;
			case "opus" -> OpusAudioStream::new;
			default -> throw new IllegalStateException("Unexpected audio file extension: " + extension);
		};
		return new RepeatingAudioStream(delegateFactory, stream);
	}

}
