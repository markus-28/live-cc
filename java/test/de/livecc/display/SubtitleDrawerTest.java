package de.livecc.display;

import de.livecc.TranscriptionPublisher;
import org.junit.Test;

public class SubtitleDrawerTest {

    @Test
    public void testConstructor() {
        TranscriptionPublisher publisher = new TranscriptionPublisher();
        new SubtitleDrawer(publisher);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullFail() {
        new SubtitleDrawer(null);
    }
}
