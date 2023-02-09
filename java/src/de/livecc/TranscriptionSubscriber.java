package de.livecc;

import com.google.cloud.speech.v1.StreamingRecognitionResult;

/**
 * If a TranscriptionSubscriber is subscribed to a TranscriptionPublisher
 * the receive-function is called when a Message is being published.
 */
public interface TranscriptionSubscriber {

    /**
     * The receive-function is called by a TranscriptionPublisher, if a subscriber subscribed
     * to it beforehand.
     *
     * @param transcription A complete Response from the Google Cloud Speech API.
     */
    void receive(StreamingRecognitionResult transcription);
}
