package de.livecc;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;

import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.StreamingRecognitionConfig;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeRequest;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import com.google.protobuf.ByteString;

import java.io.IOException;

/**
 * This class uses the Google Cloud Speech API to transcribe an audio stream
 * into text.
 */
public class GoogleSpeechRecognizer implements Runnable {

    private final MicrophoneStream microphoneStream;
    private final int sampleRateInHertz;
    private StreamingRecognitionConfig streamingRecognitionConfig;
    private final TranscriptionPublisher transcriptionPublisher;

    public GoogleSpeechRecognizer(MicrophoneStream micStream, int sampleRate, TranscriptionPublisher publisher) {
        microphoneStream = micStream;
        sampleRateInHertz = sampleRate;
        transcriptionPublisher = publisher;
        setupStreamingConfig();
    }

    private void setupStreamingConfig() {
        RecognitionConfig recognitionConfig = RecognitionConfig
                .newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                .setLanguageCode("de-DE")
                .setSampleRateHertz(sampleRateInHertz)
                .setEnableAutomaticPunctuation(true)
                .setMaxAlternatives(0)
                .build();

        streamingRecognitionConfig = StreamingRecognitionConfig
                .newBuilder()
                .setConfig(recognitionConfig)
                .setInterimResults(true)
                .build();
    }

    @Override
    public void run() {
        startSendingRequests();
    }

    private void startSendingRequests() {
        try(SpeechClient speechClient = SpeechClient.create()) {

            ClientStream<StreamingRecognizeRequest> clientStream = speechClient
                    .streamingRecognizeCallable()
                    .splitCall(responseObserver);

            sendStreamingConfig(clientStream);

            while(true) {
                sendAudioStream(clientStream);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendStreamingConfig(ClientStream<StreamingRecognizeRequest> clientStream) {
        StreamingRecognizeRequest request = StreamingRecognizeRequest
                .newBuilder()
                .setStreamingConfig(streamingRecognitionConfig)
                .build();

        clientStream.send(request);
    }

    private void sendAudioStream(ClientStream<StreamingRecognizeRequest> clientStream) throws InterruptedException {
        ByteString audioData = ByteString.copyFrom(microphoneStream.getChunk());

        StreamingRecognizeRequest request = StreamingRecognizeRequest
                .newBuilder()
                .setAudioContent(audioData)
                .build();

        clientStream.send(request);
    }

    private final ResponseObserver<StreamingRecognizeResponse> responseObserver = new ResponseObserver<>() {

        @Override
        public void onStart(StreamController streamController) {}

        @Override
        public void onResponse(StreamingRecognizeResponse response) {
            StreamingRecognitionResult transcription = response.getResultsList().get(0);
            transcriptionPublisher.publishMessage(transcription);
        }

        @Override
        public void onComplete() {}

        @Override
        public void onError(Throwable t) { t.printStackTrace(); }
    };
}
