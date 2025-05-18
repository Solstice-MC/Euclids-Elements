package org.solstice.euclidsElements.audio.api;

import de.maxhenkel.opus4j.OpusDecoder;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.BufferedAudioStream;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;

@Environment(EnvType.CLIENT)
public class OpusAudioStream implements BufferedAudioStream {

	private final InputStream stream;
	private final AudioFormat format;
	private final OpusDecoder decoder;

	public OpusAudioStream(InputStream inputStream) {
		this.stream = inputStream;
		this.decoder = null;
		this.format = null;
	}

	@Override
	public boolean read(FloatConsumer consumer) {
		return false;
	}

	@Override
	public AudioFormat getFormat() {
		return this.format;
	}

	@Override
	public void close() throws IOException {
		this.stream.close();
		this.decoder.close();
	}

}
