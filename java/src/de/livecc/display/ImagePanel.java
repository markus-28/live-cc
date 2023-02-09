package de.livecc.display;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;

/**
 * Overrides JPanel in order to be able to change a background image.
 */
class ImagePanel extends JPanel {

    private Image currentImage;

    /**
     * Create a new buffered ImagePanel with the specified layout manager.
     * @param layoutManager A layout manager.
     */
    public ImagePanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    /**
     * Sets the given image as background.
     * Repainting the Panel changes the background by calling the overridden
     * paintComponent Method.
     * @param image an Image.
     */
    public void drawImage(Image image) {
        currentImage = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(currentImage, 0, 0, 1920, 1080, null);
    }
}
