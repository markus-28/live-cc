package de.livecc;

import com.google.cloud.speech.v1.StreamingRecognitionResult;
import org.junit.Assert;
import org.junit.Test;

public class TranscriptionPublisherTest {

    class TestSubscriber implements TranscriptionSubscriber {
        boolean gotCalled = false;

        @Override
        public void receive(StreamingRecognitionResult transcription) {
            gotCalled = true;
        }
    }

    @Test
    public void testSubscribingAndSendingMessage() {
        TranscriptionPublisher publisher = new TranscriptionPublisher();
        TestSubscriber testSubscriber = new TestSubscriber();

        publisher.subscribe(testSubscriber);
        publisher.publishMessage(StreamingRecognitionResult.newBuilder().build());

        Assert.assertTrue(testSubscriber.gotCalled);
    }

    @Test
    public void testSendingMessageWithoutSubscribing() {
        TranscriptionPublisher publisher = new TranscriptionPublisher();
        TestSubscriber testSubscriber = new TestSubscriber();

        publisher.publishMessage(StreamingRecognitionResult.newBuilder().build());

        Assert.assertFalse(testSubscriber.gotCalled);
    }

    @Test
    public void testSubscribingAndUnsubscribing() {
        TranscriptionPublisher publisher = new TranscriptionPublisher();
        TestSubscriber testSubscriber = new TestSubscriber();

        publisher.subscribe(testSubscriber);
        publisher.unsubscribe(testSubscriber);
        publisher.publishMessage(StreamingRecognitionResult.newBuilder().build());

        Assert.assertFalse(testSubscriber.gotCalled);
    }

    @Test
    public void testSendingMessageWithSeveralSubscribers() {
        TranscriptionPublisher publisher = new TranscriptionPublisher();
        TestSubscriber testSubscriber1 = new TestSubscriber();
        TestSubscriber testSubscriber2 = new TestSubscriber();
        TestSubscriber testSubscriber3 = new TestSubscriber();

        publisher.subscribe(testSubscriber1);
        publisher.subscribe(testSubscriber2);
        publisher.subscribe(testSubscriber3);

        publisher.publishMessage(StreamingRecognitionResult.newBuilder().build());

        Assert.assertTrue(testSubscriber1.gotCalled);
        Assert.assertTrue(testSubscriber2.gotCalled);
        Assert.assertTrue(testSubscriber3.gotCalled);
    }
}
