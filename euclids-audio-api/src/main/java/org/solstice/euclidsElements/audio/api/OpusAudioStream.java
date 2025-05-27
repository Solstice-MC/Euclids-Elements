package org.solstice.euclidsElements.audio.api;

import de.maxhenkel.opus4j.OpusDecoder;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.BufferedAudioStream;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Environment(EnvType.CLIENT)
public class OpusAudioStream implements BufferedAudioStream {
    private static final int SAMPLE_RATE = 48000; // Opus standard sample rate
    private static final int CHANNELS = 2; // Stereo by default
    private static final float FLOAT_SCALE = 1.0f / 32768.0f; // Convert from short to float

    private final InputStream stream;
    private final AudioFormat format;
    private final OpusDecoder decoder;
    private final byte[] inputBuffer;
    private boolean endOfStream;

    public OpusAudioStream(InputStream inputStream) {
        this.stream = inputStream;
        this.inputBuffer = new byte[CHUNK_SIZE];
		ByteBuffer outputByteBuffer = ByteBuffer.allocate(CHUNK_SIZE * 2); // 2 bytes per sample
		outputByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.endOfStream = false;

        // Initialize the Opus decoder with standard parameters
        try {
            this.decoder = new OpusDecoder(SAMPLE_RATE, CHANNELS);
            // Create audio format matching the Opus decoder settings
            this.format = new AudioFormat((float) SAMPLE_RATE, 16, CHANNELS, true, false);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to initialize Opus decoder", exception);
        }
    }

    @Override
    public boolean read(FloatConsumer consumer) throws IOException {
        if (this.endOfStream) return false;

        // Read data from the input stream
        int bytesRead = this.stream.read(this.inputBuffer, 0, this.inputBuffer.length);
        if (bytesRead <= 0) {
            this.endOfStream = true;
            return false;
        }

        try {
            // Create a byte array with just the read data
            byte[] packetData = new byte[bytesRead];
            System.arraycopy(this.inputBuffer, 0, packetData, 0, bytesRead);

            // Decode the Opus packet
            // Based on the error message, the decode method takes a byte array and a boolean
            // The boolean likely indicates whether this is the last packet
            short[] decodedData = this.decoder.decode(packetData, false);
			if (decodedData.length == 0) return false;

			for (short sample : decodedData) {
				consumer.accept(sample * FLOAT_SCALE);
			}
			return true;
        } catch (Exception e) {
            throw new IOException("Failed to decode Opus audio data", e);
        }
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
