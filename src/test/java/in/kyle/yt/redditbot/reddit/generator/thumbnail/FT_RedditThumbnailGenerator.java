package in.kyle.yt.redditbot.reddit.generator.thumbnail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static net.java.quickcheck.generator.PrimitiveGeneratorsIterables.someStrings;
import static org.assertj.core.api.Assertions.assertThat;

class FT_RedditThumbnailGenerator {
    
    private final Graphics graphics =
            new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR).getGraphics();
    private final Dimension targetDimension = new Dimension(150, 250);
    
    @Test
    void testMakeLinesBasicTwo() {
        var lines = RedditThumbnailGenerator.makeLines(List.of("Hello", "World"),
                                                       graphics,
                                                       targetDimension);
        assertThat(lines).containsExactly(List.of("Hello"), List.of("World"));
    }
    
    @Test
    void testMakeLinesBasicFuzz() {
        for (String title : someStrings("abcdefghi", 1, 300)) {
            List<String> parts = List.of(title.split(" "));
            try {
                RedditThumbnailGenerator.makeLines(parts, graphics, targetDimension);
            } catch (Exception e) {
                Assertions.fail("Could not run for " + title, e);
            }
        }
    }
}
