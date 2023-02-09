package de.livecc.display.background;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Provides a black image.
 */
public class BlackImageProvider implements ImageStrategy {

    private final BufferedImage blackImage;

    public BlackImageProvider(int screenWidth, int screenHeight) {
        if(screenWidth <= 0 || screenHeight <= 0)
            throw new IllegalArgumentException();

        blackImage = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = blackImage.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);

    }

    @Override
    public synchronized BufferedImage provideImage() {
        ColorModel colorModel = blackImage.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = blackImage.copyData(null);

        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
}
