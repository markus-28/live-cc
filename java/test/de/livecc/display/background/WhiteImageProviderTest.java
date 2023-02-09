package de.livecc.display.background;

import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class WhiteImageProviderTest {
    
    @Test
    public void testConstructor() {
        new WhiteImageProvider(1920, 1080);
        new WhiteImageProvider(1080, 1080);
        new WhiteImageProvider(1, 1);
        new WhiteImageProvider(10000, 10000);
    }

    @Test
    public void testInstancesEquals() {
        BufferedImage underTest1 = new WhiteImageProvider(1920, 1080).provideImage();
        BufferedImage underTest2 = new WhiteImageProvider(1920, 1080).provideImage();

        Assert.assertTrue(ImageEqualityTestHelper.compareBufferedImages(underTest1, underTest2));

        BufferedImage underTest3 = new WhiteImageProvider(1920, 1080).provideImage();
        BufferedImage underTest4 = new WhiteImageProvider(3840, 2160).provideImage();

        Assert.assertFalse(ImageEqualityTestHelper.compareBufferedImages(underTest3, underTest4));
    }

    @Test
    public void testInstancesNotEquals() {
        BufferedImage underTest1 = new WhiteImageProvider(1920, 1080).provideImage();
        BufferedImage underTest2 = new WhiteImageProvider(3840, 2160).provideImage();

        Assert.assertNotEquals(underTest1.getHeight(), underTest2.getHeight());
        Assert.assertNotEquals(underTest1.getWidth(), underTest2.getWidth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail1() {
        new WhiteImageProvider(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail2() {
        new WhiteImageProvider(-1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail3() {
        new WhiteImageProvider(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail4() {
        new WhiteImageProvider(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail5() {
        new WhiteImageProvider(0, 1);
    }

    @Test
    public void testProvideImage() {
        WhiteImageProvider underTest1 = new WhiteImageProvider(1920, 1080);
        BufferedImage imgTest1 = underTest1.provideImage();
        Assert.assertEquals(imgTest1.getHeight(), 1080);
        Assert.assertEquals(imgTest1.getWidth(), 1920);

        WhiteImageProvider underTest2 = new WhiteImageProvider(1, 1);
        BufferedImage imgTest2 = underTest2.provideImage();
        Assert.assertEquals(imgTest2.getHeight(), 1);
        Assert.assertEquals(imgTest2.getWidth(), 1);

        WhiteImageProvider underTest3 = new WhiteImageProvider(5000, 5000);
        BufferedImage imgTest3 = underTest3.provideImage();
        Assert.assertEquals(imgTest3.getHeight(), 5000);
        Assert.assertEquals(imgTest3.getWidth(), 5000);
    }

    @Test
    public void testWhiteColor() {
        WhiteImageProvider underTest1 = new WhiteImageProvider(1920, 1080);
        BufferedImage imgTest1 = underTest1.provideImage();
        Assert.assertEquals(imgTest1.getHeight(), 1080);
        Assert.assertEquals(imgTest1.getWidth(), 1920);
        Color ret1 = new Color(imgTest1.getRGB(0,0));
        Assert.assertEquals(ret1, Color.WHITE);
    }
}
