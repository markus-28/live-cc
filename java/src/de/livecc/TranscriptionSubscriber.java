package de.livecc;

import com.google.cloud.speech.v1.StreamingRecognitionResult;

/**
 * If a TranscriptionSubscriber is subscribed to a TranscriptionPublisher
 * the receive-function is called when a Message is being published.
 */
public interface TranscriptionSubscriber {
    void receive(StreamingRecognitionResult transcription);
}
