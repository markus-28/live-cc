package de.livecc.transcription;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Connects to a Microphone and opens an audio-stream.
 * The content of the stream is written to a queue-buffer.
 */
public class MicrophoneStream implements Runnable {

    private final int rate;
    private final int chunk;
    private final BlockingQueue<byte[]> buffer;
    private boolean isOpen;

    private TargetDataLine targetDataLine;

    public MicrophoneStream(int rate) {
        this.rate = rate;
        this.chunk = rate / 10;
        this.buffer = new LinkedBlockingQueue<>();
        this.isOpen = false;
    }

    @Override
    public void run() {
        try {
            setupAudioStreamAndStartReading();
        } catch (LineUnavailableException e) {
            throw new IllegalStateException(e);
        }
    }

    private void setupAudioStreamAndStartReading() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, 1, 2, rate, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new IllegalStateException("Line not supported");
        }

        targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
        targetDataLine.open(format);
        targetDataLine.start();
        isOpen = true;

        continuouslyWriteDataIntoBuffer();
    }

    private void continuouslyWriteDataIntoBuffer() {
        while (isOpen) {
            byte[] data = new byte[chunk];
            targetDataLine.read(data, 0, data.length);
            buffer.add(data);
        }
    }

    public void stop() {
        isOpen = false;
        targetDataLine.stop();
        targetDataLine.close();
    }

    public byte[] getChunk() throws InterruptedException {
        return buffer.take();
    }
}
