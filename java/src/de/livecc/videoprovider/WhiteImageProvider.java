package de.livecc.videoprovider;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Provides a white image.
 */
public class WhiteImageProvider implements ImageStrategy {

    private BufferedImage whiteImage;

    public WhiteImageProvider(int screenWidth, int screenHeight) {

        whiteImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = whiteImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, screenWidth, screenHeight);

    }

    @Override
    public BufferedImage provideImage() {
        return whiteImage; // TODO object is mutable
    }
}
