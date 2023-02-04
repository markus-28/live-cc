package de.livecc;

import com.google.cloud.speech.v1.StreamingRecognitionResult;

import java.util.ArrayList;

/**
 * Publishes a transcription to all TranscriptionSubscribers.
 */
public class TranscriptionPublisher {

    private final ArrayList<TranscriptionSubscriber> subscribers = new ArrayList<>();

    public void subscribe(TranscriptionSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(TranscriptionSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * A StreamingRecognitionResult object is given because some Subscribers dependent
     * on the "is_final" flag.
     * @param transcription Concrete Response by the Google Speech API.
     */
    public void publishMessage(StreamingRecognitionResult transcription) {
//        String transcriptionString = transcription.getAlternativesList().get(0).getTranscript();
        subscribers.forEach(subscriber -> subscriber.receive(transcription));
    }
}
