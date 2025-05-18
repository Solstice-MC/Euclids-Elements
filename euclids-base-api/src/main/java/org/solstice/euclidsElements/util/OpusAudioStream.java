package org.solstice.euclidsElements.util;

import de.maxhenkel.opus4j.OpusDecoder;
import de.maxhenkel.opus4j.UnknownPlatformException;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.BufferedAudioStream;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;

@Environment(EnvType.CLIENT)
public class OpusAudioStream implements BufferedAudioStream {

	// TODO replace shitty ai generated code with WORKING CODE

    private static final int SAMPLE_RATE = 48000; // Opus standard sample rate
    private static final int CHANNELS = 2; // Default channels (stereo)
    private static final int MAX_FRAME_SIZE = 5760; // Maximum size for an Opus frame

    private final OpusDecoder decoder;
    private final AudioFormat format;
    private final InputStream inputStream;
    private final byte[] inputBuffer = new byte[CHUNK_SIZE];
    private final short[] decodedBuffer;
    private final float[] floatBuffer;
    private int bufferPosition = 0;
    private int bufferSize = 0;
    private boolean endOfStream = false;

    public OpusAudioStream(InputStream inputStream) throws IOException {
		this.inputStream = inputStream;

		// Read OpusHead information (could be implemented with more sophistication)
		// For simplicity, we assume a standard configuration
		try {
			this.decoder = new OpusDecoder(SAMPLE_RATE, CHANNELS);
		} catch (UnknownPlatformException exception) {
			throw new RuntimeException(exception);
		}

		// Set up buffers for decoded audio
		this.decodedBuffer = new short[MAX_FRAME_SIZE * CHANNELS];
		this.floatBuffer = new float[MAX_FRAME_SIZE * CHANNELS];

		// Create audio format
		this.format = new AudioFormat(SAMPLE_RATE, 16, CHANNELS, true, false);

		fillBuffer();
    }

    @Override
    public AudioFormat getFormat() {
        return this.format;
    }

    @Override
    public boolean read(FloatConsumer consumer) throws IOException {
        if (endOfStream && bufferPosition >= bufferSize) {
            return false;
        }

        // If buffer is depleted, fill it with more decoded data
        if (bufferPosition >= bufferSize) {
            if (!fillBuffer()) {
                return false;
            }
        }

        // Provide audio data to consumer
        while (bufferPosition < bufferSize) {
            consumer.accept(floatBuffer[bufferPosition++]);
        }

        return true;
    }

    private boolean fillBuffer() throws IOException {
        if (endOfStream) {
            return false;
        }

		// Read packet size (assuming Ogg Opus format with 2-byte size header)
		byte[] sizeBuffer = new byte[2];
		int bytesRead = readFully(sizeBuffer, 0, 2);
		if (bytesRead < 2) {
			endOfStream = true;
			return false;
		}

		int packetSize = ((sizeBuffer[0] & 0xFF) | ((sizeBuffer[1] & 0xFF) << 8));
		if (packetSize <= 0 || packetSize > CHUNK_SIZE) {
			throw new IOException("Invalid Opus packet size: " + packetSize);
		}

		// Read packet data
		bytesRead = readFully(inputBuffer, 0, packetSize);
		if (bytesRead < packetSize) {
			endOfStream = true;
			return false;
		}

		// Decode packet
		short[] samplesDecoded = decoder.decode(inputBuffer, false);
		this.decoder.resetState();

		// Convert to float
		convertToFloat(decodedBuffer, floatBuffer, CHUNK_SIZE * CHANNELS);

		// Reset buffer position and set new size
		bufferPosition = 0;
		bufferSize = CHUNK_SIZE * CHANNELS;

		return true;
    }

    private int readFully(byte[] buffer, int offset, int length) throws IOException {
        int totalBytesRead = 0;
        while (totalBytesRead < length) {
            int bytesRead = inputStream.read(buffer, offset + totalBytesRead, length - totalBytesRead);
            if (bytesRead == -1) {
                break;
            }
            totalBytesRead += bytesRead;
        }
        return totalBytesRead;
    }

    private void convertToFloat(short[] input, float[] output, int length) {
        // Convert 16-bit PCM to float in range [-1.0, 1.0]
        for (int i = 0; i < length; i++) {
            output[i] = input[i] / 32768.0f;
        }
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
        this.decoder.close();
    }
}
