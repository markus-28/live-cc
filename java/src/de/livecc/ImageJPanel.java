package de.livecc;

import javax.swing.*;
import java.awt.*;

/**
 * Overrides JPanel in order to continuously change a background image.
 */
class ImageJPanel extends JPanel {

    /**
     * Create a new buffered ImageJPanel with the specified layout manager.
     * @param layoutManager A layout manager.
     */
    public ImageJPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    /**
     * Draws the given Image as background.
     * @param image an Image.
     */
    public void drawImage(Image image) {
        this.getGraphics().drawImage(image, 0, 0, this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
