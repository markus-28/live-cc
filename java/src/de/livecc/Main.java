package de.livecc;

public class Main {

    private static final int SAMPLE_RATE_IN_HERTZ = 48000;

    public static void main(String[] args) {
        TranscriptionPublisher transcriptionPublisher = new TranscriptionPublisher();

        SubtitleDrawer subtitleDrawer = new SubtitleDrawer(transcriptionPublisher);
        Thread subDrawerThread = new Thread(subtitleDrawer);
        subDrawerThread.start();

        MicrophoneStream micStream = new MicrophoneStream(SAMPLE_RATE_IN_HERTZ);
        Thread micThread = new Thread(micStream);
        micThread.start();

        GoogleSpeechRecognizer recognizer = new GoogleSpeechRecognizer(micStream, SAMPLE_RATE_IN_HERTZ, transcriptionPublisher);
        Thread recognizerThread = new Thread(recognizer);
        recognizerThread.start();
    }
}
