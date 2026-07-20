package ph.edu.dlsu.lbycpob.hangman.render;
import ph.edu.dlsu.lbycpob.hangman.utils.ClasspathResources;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * {@link HangmanRenderer} that reads pre-drawn ASCII art files bundled as
 * classpath resources and <em>returns</em> their lines.
 *
 * <p>The only change from the original: the for-loop that called
 * {@code IO.println(line)} is gone. Returning the lines and letting the
 * caller decide how to display them is the correct separation of concerns
 * for a server-side web component.
 */
public final class AsciiArtRenderer implements HangmanRenderer {
    private static final int MIN_GUESSES_REMAINING = 0;
    private static final int MAX_GUESSES_REMAINING = 8;

    private final String resourceBasePath;

    public AsciiArtRenderer(String resourceBasePath) {
        Objects.requireNonNull(resourceBasePath, "resourceBasePath must not be null");
        if (resourceBasePath.isBlank()) {
            throw new IllegalArgumentException("resourceBasePath must not be blank");
        }
        this.resourceBasePath = resourceBasePath.endsWith("/")
                ? resourceBasePath.substring(0, resourceBasePath.length() - 1)
                : resourceBasePath;
    }

    @Override
    public List<String> render(int guessesRemaining) throws IOException {
        if (guessesRemaining < MIN_GUESSES_REMAINING
                || guessesRemaining > MAX_GUESSES_REMAINING) {
            throw new IllegalArgumentException(
                    "guessesRemaining must be between " + MIN_GUESSES_REMAINING
                            + " and " + MAX_GUESSES_REMAINING
                            + ", got " + guessesRemaining);
        }
        String resourcePath = resourceBasePath + "/display" + guessesRemaining + ".txt";
        return ClasspathResources.readLines(resourcePath);   // was: for-loop + IO.println
    }
}