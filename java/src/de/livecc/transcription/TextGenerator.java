package de.livecc.transcription;

import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.StreamingRecognitionResult;
import com.google.cloud.speech.v1.StreamingRecognizeResponse;
import de.livecc.TranscriptionPublisher;

/**
 * Replaces the GoogleSpeechRecognizer with a text generator for testing purposes.
 * To the Outside it acts similar to the GoogleSpeechRecognizer.
 */
public class TextGenerator implements Runnable {

    private static final String goetheFaustString = "An einem trüben Tag auf freiem Feld erfährt Faust, dass Gretchen in ihrer Verzweiflung ihr neugeborenes " +
            "Kind ertränkt hat. Sie ist zum Tode verurteilt und wartet im Kerker auf ihre Hinrichtung. Eine Rückkehr " +
            "in die Stadt birgt Gefahren, dennoch will Faust Gretchen retten. Er macht Mephisto für das Unglück verantwortlich " +
            "und verlangt, dass er ihm bei der Befreiung helfe. Mephisto weist jegliche Schuld von sich. Schließlich " +
            "sei es Faust gewesen, der Gretchen begehrt und geschwängert habe. Mephisto erklärt sich nur bereit, den " +
            "Wächter einzuschläfern und Zauberpferde für die Flucht zu stellen; Gretchens Befreiung müsse Faust " +
            "selbst durchführen. Faust dringt in den Kerker ein und versucht Gretchen zu überzeugen, mit ihm zu fliehen. " +
            "Doch aus Angst, noch weiter in die Verderblichkeit gezogen zu werden, lehnt Gretchen Fausts Hilfe ab. " +
            "Sie wendet sich Gott zu, und wird von ihren Sünden erlöst. " +
            "An einem trüben Tag auf freiem Feld erfährt Faust, dass Gretchen in ihrer Verzweiflung ihr neugeborenes Kind " +
            "ertränkt hat. Sie ist zum Tode verurteilt und wartet im Kerker auf ihre Hinrichtung. Eine Rückkehr in die " +
            "Stadt birgt Gefahren, dennoch will Faust Gretchen retten. Er macht Mephisto für das Unglück verantwortlich " +
            "und verlangt, dass er ihm bei der Befreiung helfe. Mephisto weist jegliche Schuld von sich. Schließlich " +
            "sei es Faust gewesen, der Gretchen begehrt und geschwängert habe. Mephisto erklärt sich nur bereit, den " +
            "Wächter einzuschläfern und Zauberpferde für die Flucht zu stellen; Gretchens Befreiung müsse Faust " +
            "selbst durchführen. Faust dringt in den Kerker ein und versucht Gretchen zu überzeugen, mit ihm zu fliehen. " +
            "Doch aus Angst, noch weiter in die Verderblichkeit gezogen zu werden, lehnt Gretchen Fausts Hilfe ab. " +
            "Sie wendet sich Gott zu, und wird von ihren Sünden erlöst.";

    private final StringBuilder currentSentence = new StringBuilder();

    private final TranscriptionPublisher transcriptionPublisher;

    public TextGenerator(TranscriptionPublisher publisher) {
        this.transcriptionPublisher = publisher;
    }

    @Override
    public void run() {
        startGeneratingText();
    }

    private void startGeneratingText() {
        String[] sentences = goetheFaustString.split(" ");
        for(String word : sentences) {
            sendMockUpTranscription(word);
        }
    }

    private void sendMockUpTranscription(String word) {

        StreamingRecognitionResult result = buildMockUpResult(word);
        transcriptionPublisher.publishMessage(result);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private StreamingRecognitionResult buildMockUpResult(String word) {
        currentSentence.append(" ").append(word);

        StreamingRecognizeResponse mockUpTranscription;
        if(word.charAt(word.length() - 1) == '.') {
            mockUpTranscription = StreamingRecognizeResponse.newBuilder()
                    .addResults(StreamingRecognitionResult.newBuilder()
                            .addAlternatives(SpeechRecognitionAlternative.newBuilder()
                                    .setTranscript(currentSentence.toString())
                                    .build())
                            .setIsFinal(true)
                            .build())
                    .build();
            currentSentence.setLength(0); // clearing StringBuilder as this transcript is final.
        }
        else {
            mockUpTranscription = StreamingRecognizeResponse.newBuilder()
                    .addResults(StreamingRecognitionResult.newBuilder()
                            .addAlternatives(SpeechRecognitionAlternative.newBuilder()
                                    .setTranscript(currentSentence.toString())
                                    .build())
                            .build())
                    .build();
        }

        return mockUpTranscription.getResults(0);
    }
}
