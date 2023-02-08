package de.livecc;

import com.google.cloud.speech.v1.StreamingRecognitionResult;
import de.livecc.videoprovider.BlackImageProvider;
import de.livecc.videoprovider.ImageContext;
import de.livecc.videoprovider.ImageStrategy;
import de.livecc.videoprovider.WhiteImageProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A SubtitleDrawer draws subtitles received by a publisher to a given Image/Video.
 */
public class SubtitleDrawer implements TranscriptionSubscriber, Runnable {

    private static final int MAX_CAPACITY = 500;
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private JFrame jFrame;

    private ImageJPanel backgroundPanel;

    private JLabel textBlockLower;
    private JLabel textBlockUpper;

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
        jFrame.setUndecorated(true);
        jFrame.setVisible(true);

        fontMetrics = jFrame.getGraphics().getFontMetrics(font);
        Dimension preferredJLabelSize = new Dimension(SCREEN_SIZE.width, fontMetrics.getHeight());

        textBlockUpper = new JLabel(upperSubtitleBlock);
        textBlockUpper.setLayout(null);
        textBlockUpper.setVisible(true);
        textBlockUpper.setFont(font);
        textBlockUpper.setOpaque(false);
        textBlockUpper.setPreferredSize(preferredJLabelSize);

        textBlockLower = new JLabel(lowerSubtitleBlock);
        textBlockLower.setLayout(null);
        textBlockLower.setVisible(true);
        textBlockLower.setFont(font);
        textBlockLower.setOpaque(false);
        textBlockLower.setPreferredSize(preferredJLabelSize);

        JPanel subtitlePanel = new JPanel();
        subtitlePanel.setLayout(new BoxLayout(subtitlePanel, BoxLayout.Y_AXIS));
        subtitlePanel.setOpaque(false);
        subtitlePanel.add(textBlockUpper);
        subtitlePanel.add(textBlockLower);

        backgroundPanel = new ImageJPanel(new BorderLayout());
        backgroundPanel.setBackground(Color.GREEN);
        backgroundPanel.add(subtitlePanel, BorderLayout.SOUTH);

        jFrame.getContentPane().add(backgroundPanel);

    }

    @Override
    public void run() {
        transcriptionPublisher.subscribe(this);

        Thread bgThread = new Thread(this::manageBackground);
        bgThread.start();
    }

    private void manageBackground() {

        ImageStrategy currentStrategy = new BlackImageProvider(SCREEN_SIZE.width, SCREEN_SIZE.height);
        ImageContext imageContext = new ImageContext(currentStrategy);

        while(true) {
            BufferedImage image = imageContext.executeStrategy();
            backgroundPanel.drawImage(image);

            if(currentStrategy instanceof WhiteImageProvider)
                currentStrategy = new BlackImageProvider(SCREEN_SIZE.width, SCREEN_SIZE.height);
            else
                currentStrategy = new WhiteImageProvider(SCREEN_SIZE.width, SCREEN_SIZE.height);

            imageContext.setImageStrategy(currentStrategy);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void receive(StreamingRecognitionResult transcription) {

        String transcriptionString = transcription.getAlternatives(0).getTranscript();
        boolean isFinal = transcription.getIsFinal();

        String subtitlesToDraw = mostRecentSubtitleStorage + transcriptionString;

        if(isFinal) {
            mostRecentSubtitleStorage = mostRecentSubtitleStorage + transcriptionString;

            int storageLength = mostRecentSubtitleStorage.length();
            if(storageLength > MAX_CAPACITY) {
                mostRecentSubtitleStorage = mostRecentSubtitleStorage.substring(storageLength - MAX_CAPACITY);
            }

            subtitlesToDraw = mostRecentSubtitleStorage;
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
