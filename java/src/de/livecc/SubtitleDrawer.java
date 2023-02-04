package de.livecc;

import com.google.cloud.speech.v1.StreamingRecognitionResult;

import javax.swing.*;
import java.awt.*;

/**
 * A SubtitleDrawer draws subtitles received by a publisher to a given Image/Video.
 */
public class SubtitleDrawer implements TranscriptionSubscriber, Runnable {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private JLabel textBlockUpper;
//    private JLabel textBlockLower;

    private JFrame jFrame;
    private final TranscriptionPublisher transcriptionPublisher;

    private String upperSubtitleBlock = "222";
//    private String lowerSubtitleBlock = "";

    public SubtitleDrawer(TranscriptionPublisher publisher) {
        transcriptionPublisher = publisher;
        setupJFrameAndText();
    }

    private void setupJFrameAndText() {
        textBlockUpper = new JLabel(upperSubtitleBlock, SwingConstants.CENTER);
        textBlockUpper.setLayout(null);
        textBlockUpper.setVisible(true);
        textBlockUpper.setFont(new Font("Arial", Font.PLAIN, 30));


        jFrame = new JFrame("window");
        jFrame.getContentPane().add(textBlockUpper);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.getContentPane().setBackground(Color.RED);
        jFrame.setUndecorated(true);
        jFrame.setVisible(true);
    }

    @Override
    public void run() {
        transcriptionPublisher.subscribe(this);
    }

    @Override
    public void receive(StreamingRecognitionResult transcription) {
        String transcriptionString = transcription.getAlternatives(0).getTranscript();

        System.out.println(transcriptionString);

//        upperSubtitleBlock = transcriptionString;
        textBlockUpper.setText(transcriptionString);
    }
}
