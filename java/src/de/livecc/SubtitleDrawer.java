package de.livecc;

import com.google.cloud.speech.v1.StreamingRecognitionResult;

import javax.swing.*;
import java.awt.*;

/**
 * A SubtitleDrawer draws subtitles received by a publisher to a given Image/Video.
 */
public class SubtitleDrawer implements TranscriptionSubscriber, Runnable {

    private static final int MAX_CAPACITY = 200;
    private static final int MAX_BLOCK_LENGTH = 90;
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private JLabel textBlockLower;
    private JLabel textBlockUpper;
    private JFrame jFrame;

    private final TranscriptionPublisher transcriptionPublisher;

    private String mostRecentSubtitleStorage = "";

    private String match = "\n";
    private String upperSubtitleBlock = "";
    private String lowerSubtitleBlock = "";

    public SubtitleDrawer(TranscriptionPublisher publisher) {
        transcriptionPublisher = publisher;
        setupJFrameAndText();
    }

    private void setupJFrameAndText() {
        textBlockUpper = new JLabel(upperSubtitleBlock, SwingConstants.LEADING);
        textBlockUpper.setLayout(null);
        textBlockUpper.setVisible(true);
        textBlockUpper.setFont(new Font("Arial", Font.PLAIN, 30));
        textBlockUpper.setBackground(Color.GREEN);
        textBlockUpper.setOpaque(true);

        textBlockLower = new JLabel(lowerSubtitleBlock);
        textBlockLower.setLayout(null);
        textBlockLower.setVisible(true);
        textBlockLower.setFont(new Font("Arial", Font.PLAIN, 30));
        textBlockLower.setBackground(Color.GREEN);
        textBlockLower.setOpaque(true);

        JPanel subtitlePanel = new JPanel();
        subtitlePanel.add(textBlockUpper);
        subtitlePanel.add(textBlockLower);
        subtitlePanel.setLayout(new BoxLayout(subtitlePanel, BoxLayout.Y_AXIS));
        subtitlePanel.setBackground(Color.CYAN);
        subtitlePanel.setOpaque(true);

        jFrame = new JFrame("window");
        jFrame.getContentPane().add(subtitlePanel, BorderLayout.SOUTH);
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
        String transcriptionString = transcription.getAlternatives(0).getTranscript(); //.strip();
        boolean isFinal = transcription.getIsFinal();

        String subtitlesToDraw = mostRecentSubtitleStorage;
        if(isFinal) {
            mostRecentSubtitleStorage += transcriptionString;
            subtitlesToDraw = mostRecentSubtitleStorage;
        }
        else {
            subtitlesToDraw += transcriptionString;
        }

        makeSubtitleBoxes(subtitlesToDraw);
        updateSubtitles();
    }

    /**
     * Is called from another thread to update content for subtitle display.
     * The subtitles appear in 2 rows so that they are easier to read.
     */
    private void makeSubtitleBoxes(String subtitles) {
        String[] toDisplayList = subtitles.split(match);
        String toDisplay = toDisplayList[toDisplayList.length - 1];

        upperSubtitleBlock = buildSublist(toDisplay);

        for(int i = 0; i < upperSubtitleBlock.length() - 1; i++) {
            toDisplay = toDisplay.substring(1);
        }

        lowerSubtitleBlock = buildSublist(toDisplay);

        for(int i = 0; i < lowerSubtitleBlock.length() - 1; i++) {
            toDisplay = toDisplay.substring(1);
        }

        if (!toDisplay.equals("")) {
            match = upperSubtitleBlock;
            makeSubtitleBoxes(subtitles);
        }
    }

    private String buildSublist(String input_string) {
        String block = "";
        String[] word_list = input_string.split(" ");
        for (String word : word_list) {
            if (block.length() + word.length() >= MAX_BLOCK_LENGTH)
                break;
            block = block + word + " ";
        }
        return block;
    }

    private void updateSubtitles() {
        textBlockUpper.setText(upperSubtitleBlock);
        textBlockLower.setText(lowerSubtitleBlock);
    }
}
