package de.livecc.display;

import com.google.cloud.speech.v1.StreamingRecognitionResult;
import de.livecc.TranscriptionPublisher;
import de.livecc.TranscriptionSubscriber;
import de.livecc.display.background.BlackImageProvider;
import de.livecc.display.background.ImageContext;
import de.livecc.display.background.ImageStrategy;
import de.livecc.display.background.WhiteImageProvider;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * Opens a Fullscreen JFrame Window that displays subtitles.
 * The background of the window can be altered. For that, different ImageStrategy implementations
 * provide Images that then can be displayed by the SubtitleDrawer.
 * <p>
 * The subtitles are provided by receiving messages from a TranscriptionPublisher.
 * For that, SubtitleDrawer implements the TranscriptionSubscriber interface.
 * After subscribing, the TranscriptionPublisher will forward messages to SubtitleDrawer.
 */
public class SubtitleDrawer implements TranscriptionSubscriber, Runnable {

    private static final int SUBTITLE_STORAGE_MAX_CAPACITY = 500;
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Color SUBTITLE_COLOR = Color.RED;
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.PLAIN, 34);

    private final JFrame jFrame = new JFrame("window");
    private final TranscriptionPublisher transcriptionPublisher;
    private ImagePanel backgroundPanel;
    private JLabel textBlockLower;
    private JLabel textBlockUpper;
    private FontMetrics fontMetrics;
    private String mostRecentSubtitleStorage = "";
    private String matchingBlock = "\n";
    private String upperSubtitleBlock = "";
    private String lowerSubtitleBlock = "";

    /**
     * Creates a new instance of SubtitleDrawer. New subtitles are gathered by subscribing to the
     * given TranscriptionPublisher.
     *
     * @param publisher TranscriptionPublisher that provides new subtitles to display.
     */
    public SubtitleDrawer(TranscriptionPublisher publisher) {
        transcriptionPublisher = publisher;
        setupGraphics();
    }

    private void setupGraphics() {

        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setUndecorated(true);
        jFrame.setVisible(true);

        fontMetrics = jFrame.getGraphics().getFontMetrics(SUBTITLE_FONT);
        Dimension preferredJLabelSize = new Dimension(SCREEN_SIZE.width, fontMetrics.getHeight());

        textBlockUpper = new JLabel(upperSubtitleBlock);
        textBlockUpper.setFont(SUBTITLE_FONT);
        textBlockUpper.setForeground(SUBTITLE_COLOR);
        textBlockUpper.setPreferredSize(preferredJLabelSize);

        textBlockLower = new JLabel(lowerSubtitleBlock);
        textBlockLower.setFont(SUBTITLE_FONT);
        textBlockLower.setForeground(SUBTITLE_COLOR);
        textBlockLower.setPreferredSize(preferredJLabelSize);

        JPanel subtitlePanel = new JPanel();
        subtitlePanel.setLayout(new BoxLayout(subtitlePanel, BoxLayout.Y_AXIS));
        subtitlePanel.setOpaque(false);
        subtitlePanel.add(textBlockUpper);
        subtitlePanel.add(textBlockLower);

        backgroundPanel = new ImagePanel(new BorderLayout());
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
                Thread.sleep(500);
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
            if(storageLength > SUBTITLE_STORAGE_MAX_CAPACITY) {
                mostRecentSubtitleStorage = mostRecentSubtitleStorage.substring(storageLength - SUBTITLE_STORAGE_MAX_CAPACITY);
            }

            subtitlesToDraw = mostRecentSubtitleStorage;
        }

        makeSubtitleBoxes(subtitlesToDraw);
        updateSubtitles();

    }

    private void makeSubtitleBoxes(String subtitles) {

        String[] toDisplayList = subtitles.split(matchingBlock);
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
            matchingBlock = upperSubtitleBlock;
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

    /**
     * Overrides JPanel in order to be able to change a background image.
     */
    private static class ImagePanel extends JPanel {

        private Image currentImage;

        /**
         * Create a new buffered ImagePanel with the specified layout manager.
         *
         * @param layoutManager A layout manager.
         */
        public ImagePanel(LayoutManager layoutManager) {
            super(layoutManager);
        }

        /**
         * Sets the given image as background.
         * Repainting the Panel changes the background by calling the overridden paintComponent Method.
         *
         * @param image an Image.
         */
        public void drawImage(Image image) {
            currentImage = image;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(currentImage, 0, 0, SubtitleDrawer.SCREEN_SIZE.width, SubtitleDrawer.SCREEN_SIZE.height, null);
        }
    }
}
