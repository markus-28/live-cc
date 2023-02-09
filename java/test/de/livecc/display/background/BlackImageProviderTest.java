package de.livecc.display.background;

import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BlackImageProviderTest {

    @Test
    public void testConstructor() {
        new BlackImageProvider(1920, 1080);
        new BlackImageProvider(1080, 1080);
        new BlackImageProvider(1, 1);
        new BlackImageProvider(10000, 10000);
    }

    @Test
    public void testInstancesEquals() {
        BufferedImage underTest1 = new BlackImageProvider(1920, 1080).provideImage();
        BufferedImage underTest2 = new BlackImageProvider(1920, 1080).provideImage();

        Assert.assertTrue(ImageEqualityTestHelper.compareBufferedImages(underTest1, underTest2));
    }

    @Test
    public void testInstancesNotEquals() {
        BufferedImage underTest1 = new BlackImageProvider(1920, 1080).provideImage();
        BufferedImage underTest2 = new BlackImageProvider(3840, 2160).provideImage();
        BufferedImage underTest3 = new BlackImageProvider(1920, 2160).provideImage();
        BufferedImage underTest4 = new BlackImageProvider(3840, 1080).provideImage();

        Assert.assertFalse(ImageEqualityTestHelper.compareBufferedImages(underTest1, underTest2));
        Assert.assertFalse(ImageEqualityTestHelper.compareBufferedImages(underTest3, underTest4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail1() {
        new BlackImageProvider(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail2() {
        new BlackImageProvider(-1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail3() {
        new BlackImageProvider(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail4() {
        new BlackImageProvider(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail5() {
        new BlackImageProvider(0, 1);
    }

    @Test
    public void testProvideImage() {
        BlackImageProvider underTest1 = new BlackImageProvider(1920, 1080);
        BufferedImage imgTest1 = underTest1.provideImage();
        Assert.assertEquals(imgTest1.getHeight(), 1080);
        Assert.assertEquals(imgTest1.getWidth(), 1920);

        BlackImageProvider underTest2 = new BlackImageProvider(1, 1);
        BufferedImage imgTest2 = underTest2.provideImage();
        Assert.assertEquals(imgTest2.getHeight(), 1);
        Assert.assertEquals(imgTest2.getWidth(), 1);

        BlackImageProvider underTest3 = new BlackImageProvider(5000, 5000);
        BufferedImage imgTest3 = underTest3.provideImage();
        Assert.assertEquals(imgTest3.getHeight(), 5000);
        Assert.assertEquals(imgTest3.getWidth(), 5000);
    }

    @Test
    public void testBlackColor() {
        BlackImageProvider underTest1 = new BlackImageProvider(1920, 1080);
        BufferedImage imgTest1 = underTest1.provideImage();
        Assert.assertEquals(imgTest1.getHeight(), 1080);
        Assert.assertEquals(imgTest1.getWidth(), 1920);
        Color ret1 = new Color(imgTest1.getRGB(0,0));
        Assert.assertEquals(ret1, Color.BLACK);
    }
}
