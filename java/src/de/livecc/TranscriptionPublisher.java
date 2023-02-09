package de.livecc;

import com.google.cloud.speech.v1.StreamingRecognitionResult;

import java.util.ArrayList;

/**
 * Publishes a transcription message to all TranscriptionSubscribers.
 */
public class TranscriptionPublisher {

    private final ArrayList<TranscriptionSubscriber> subscribers = new ArrayList<>();

    /**
     * A TranscriptionSubscriber can subscribe to receiving messages from a TranscriptionPublisher.
     * All subscribers receive Method is invoked if the publisher publishes a new message.
     *
     * @param subscriber The subscriber that subscribes to this publisher.
     */
    public void subscribe(TranscriptionSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Unsubscribing this publisher. If the subscriber did not subscribe the publisher before, nothing happens.
     *
     * @param subscriber A subscriber that resigns from receiving messages.
     */
    public void unsubscribe(TranscriptionSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * A StreamingRecognitionResult object is given because some Subscribers dependent
     * on the "is_final" flag.
     *
     * @param transcription Concrete Response by an API.
     */
    public void publishMessage(StreamingRecognitionResult transcription) {
        subscribers.forEach(subscriber -> subscriber.receive(transcription));
    }
}
