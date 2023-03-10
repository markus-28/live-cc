package de.livecc.display.background;

import java.awt.image.BufferedImage;

/**
 * Context for ImageStrategy. One can alter the strategy at
 * runtime to use different implementations.
 */
public class ImageContext {

    /**
     * The ImageStrategy that is currently used.
     */
    private ImageStrategy imageStrategy;

    /**
     * Creates an ImageContext with the specified ImageStrategy.
     *
     * @param strategy an ImageStrategy.
     */
    public ImageContext(ImageStrategy strategy) {
        if(strategy == null)
            throw new NullPointerException();

        imageStrategy = strategy;
    }

    /**
     * Chooses BlackImageProvider as default ImageStrategy.
     *
     * @param screenWidth Width of screen.
     * @param screenHeight Height of screen.
     */
    public ImageContext(int screenWidth, int screenHeight) {
        if(screenWidth <= 0 || screenHeight <= 0)
            throw new IllegalArgumentException();

        imageStrategy = new BlackImageProvider(screenWidth, screenHeight);
    }

    /**
     * Alter the currently used ImageStrategy.
     *
     * @param strategy An ImageStrategy.
     */
    public void setImageStrategy(ImageStrategy strategy) {
        if(strategy == null)
            throw new NullPointerException();

        imageStrategy = strategy;
    }

    /**
     * Executes the given strategy and provides its output.
     *
     * @return A BufferedImage provided by the given strategy.
     */
    public BufferedImage executeStrategy() {
        BufferedImage image = imageStrategy.provideImage();
        return image;
    }
}
