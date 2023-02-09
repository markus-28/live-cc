package de.livecc.display.background;

import org.junit.Assert;
import org.junit.Test;

public class ImageContextTest {

    private static final int width = 1920;
    private static final int height = 1080;

    @Test
    public void testImageContextCreation() {
        ImageStrategy strategyWhite = new WhiteImageProvider(width, height);
        ImageStrategy strategyBlack = new BlackImageProvider(width, height);

        ImageContext underTest = new ImageContext(strategyWhite);

        Assert.assertTrue(ImageEqualityTestHelper.compareBufferedImages(strategyWhite.provideImage(), underTest.executeStrategy()));
        Assert.assertFalse(ImageEqualityTestHelper.compareBufferedImages(strategyBlack.provideImage(), underTest.executeStrategy()));
    }

    @Test(expected = NullPointerException.class)
    public void testImageContextCreationNullFail() {
        new ImageContext(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImageContextCreationFail1() {
        new ImageContext(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImageContextCreationFail2() {
        new ImageContext(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImageContextCreationFail3() {
        new ImageContext(-1, 1);
    }

    @Test
    public void testImageContextChangeStrategy() {
        ImageStrategy strategyWhite = new WhiteImageProvider(width, height);
        ImageStrategy strategyBlack = new BlackImageProvider(width, height);
        ImageContext underTest = new ImageContext(strategyWhite);

        underTest.setImageStrategy(strategyBlack);

        Assert.assertTrue(ImageEqualityTestHelper.compareBufferedImages(strategyBlack.provideImage(), underTest.executeStrategy()));
        Assert.assertFalse(ImageEqualityTestHelper.compareBufferedImages(strategyWhite.provideImage(), underTest.executeStrategy()));
    }

    @Test(expected = NullPointerException.class)
    public void testImageContextChangeStrategyNullFail() {
        ImageContext underTest = new ImageContext(new WhiteImageProvider(width, height));
        underTest.setImageStrategy(null);
    }

    @Test
    public void testImageContextCreationDefault() {
        ImageStrategy strategyWhite = new WhiteImageProvider(width, height);
        ImageStrategy strategyBlack = new BlackImageProvider(width, height);

        ImageContext underTest = new ImageContext(width, height);

        Assert.assertTrue(ImageEqualityTestHelper.compareBufferedImages(strategyBlack.provideImage(), underTest.executeStrategy()));
        Assert.assertFalse(ImageEqualityTestHelper.compareBufferedImages(strategyWhite.provideImage(), underTest.executeStrategy()));
    }

    @Test
    public void testImageContextChangeStrategyDefault() {
        ImageStrategy strategyWhite = new WhiteImageProvider(width, height);
        ImageStrategy strategyBlack = new BlackImageProvider(width, height);
        ImageContext underTest = new ImageContext(width, height);

        underTest.setImageStrategy(strategyWhite);

        Assert.assertTrue(ImageEqualityTestHelper.compareBufferedImages(strategyWhite.provideImage(), underTest.executeStrategy()));
        Assert.assertFalse(ImageEqualityTestHelper.compareBufferedImages(strategyBlack.provideImage(), underTest.executeStrategy()));
    }
}
