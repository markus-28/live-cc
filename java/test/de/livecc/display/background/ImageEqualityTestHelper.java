package de.livecc.display.background;

import java.awt.image.BufferedImage;

/**
 * Testing class for comparing two Images.
 */
class ImageEqualityTestHelper {

    /**
     * Compares two BufferedImages for equality.
     * Dirty way to do compare each pixel but the runtime will always stay
     * within acceptable durations, since the Resolution of Images is generally capped.
     *
     * @param expected expected BufferedImage.
     * @param actual actual BufferedImage.
     * @return True if the BufferedImages have the same pixels.
     */
    static boolean compareBufferedImages(BufferedImage expected, BufferedImage actual) {
        int expectedWidth  = expected.getWidth();
        int expectedHeight = expected.getHeight();
        int actualWidth  = actual.getWidth();
        int actualHeight = actual.getHeight();
        if (expectedWidth != actualWidth || expectedHeight != actualHeight) {
            return false;
        }

        for (int y = 0; y < expectedHeight; y++) {
            for (int x = 0; x < expectedWidth; x++) {
                if (expected.getRGB(x, y) != actual.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
}
