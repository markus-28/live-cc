package de.livecc.videoprovider;

import java.awt.image.BufferedImage;

/**
 * Strategy providing an Image from any specific ImageStrategy implementation.
 */
public interface ImageStrategy {

    /**
     * Provides an Image.
     * A concrete implementation is thread safe.
     * @return An ImageIcon.
     */
    BufferedImage provideImage();
}
