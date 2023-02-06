package de.livecc;

import com.google.cloud.speech.v1.StreamingRecognitionResult;

import javax.swing.*;
import java.awt.*;

/**
 * A SubtitleDrawer draws subtitles received by a publisher to a given Image/Video.
 */
public class SubtitleDrawer implements TranscriptionSubscriber, Runnable {

    private static final int MAX_CAPACITY = 500;
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private JLabel textBlockLower;
    private JLabel textBlockUpper;
    private JFrame jFrame;

    private final Font font = new Font("Arial", Font.PLAIN, 30);
    private FontMetrics fontMetrics;

    private final TranscriptionPublisher transcriptionPublisher;

    private String mostRecentSubtitleStorage = "";

    private String match = "\n";
    private String upperSubtitleBlock = "";
    private String lowerSubtitleBlock = "";

    public SubtitleDrawer(TranscriptionPublisher publisher) {
        transcriptionPublisher = publisher;

        setupGraphics();
    }

    private void setupGraphics() {
        jFrame = new JFrame("window");
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.getContentPane().setBackground(Color.RED);
        jFrame.setUndecorated(true);
        jFrame.setVisible(true);

        JPanel subtitlePanel = new JPanel();
        subtitlePanel.setLayout(new BoxLayout(subtitlePanel, BoxLayout.Y_AXIS));
        subtitlePanel.setBackground(Color.CYAN);
        subtitlePanel.setOpaque(true);

        jFrame.getContentPane().add(subtitlePanel, BorderLayout.SOUTH);

        Graphics graphics = jFrame.getGraphics();
        fontMetrics = graphics.getFontMetrics(font);
        int fontHeight = fontMetrics.getHeight();
        Dimension preferredJLabelSize = new Dimension(SCREEN_SIZE.width, fontHeight);

        textBlockUpper = new JLabel(upperSubtitleBlock);
        textBlockUpper.setLayout(null);
        textBlockUpper.setVisible(true);
        textBlockUpper.setFont(font);
        textBlockUpper.setBackground(Color.GREEN);
        textBlockUpper.setOpaque(true);
        textBlockUpper.setPreferredSize(preferredJLabelSize);

        textBlockLower = new JLabel(lowerSubtitleBlock);
        textBlockLower.setLayout(null);
        textBlockLower.setVisible(true);
        textBlockLower.setFont(font);
        textBlockLower.setBackground(Color.GREEN);
        textBlockLower.setOpaque(true);
        textBlockLower.setPreferredSize(preferredJLabelSize);

        subtitlePanel.add(textBlockUpper);
        subtitlePanel.add(textBlockLower);
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
            String potentialNewBlock = block + word + " ";
            int newBlockLength = fontMetrics.stringWidth(potentialNewBlock);

            if (newBlockLength >= SCREEN_SIZE.width)
                break;

            block = potentialNewBlock;
        }
        return block;
    }

    private void updateSubtitles() {
        upperSubtitleBlock = upperSubtitleBlock.strip();
        lowerSubtitleBlock = lowerSubtitleBlock.strip();
        textBlockUpper.setText(upperSubtitleBlock);
        textBlockLower.setText(lowerSubtitleBlock);
    }
}
