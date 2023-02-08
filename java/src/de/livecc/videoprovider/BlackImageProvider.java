package de.livecc.videoprovider;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Provides a black image.
 */
public class BlackImageProvider implements ImageStrategy {

    private final BufferedImage blackImage;

    public BlackImageProvider(int screenWidth, int screenHeight) {

        blackImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = blackImage.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);

    }

    @Override
    public synchronized BufferedImage provideImage() {
        return blackImage; // TODO object is mutable
    }
}
