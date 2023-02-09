package de.livecc.display.background;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

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
        ColorModel colorModel = whiteImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = whiteImage.copyData(null);

        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
}
