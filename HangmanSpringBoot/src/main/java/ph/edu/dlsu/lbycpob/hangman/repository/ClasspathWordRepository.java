package ph.edu.dlsu.lbycpob.hangman.repository;

import ph.edu.dlsu.lbycpob.hangman.render.AsciiArtRenderer;
import ph.edu.dlsu.lbycpob.hangman.utils.ClasspathResources;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * {@link WordRepository} that reads one of the word lists bundled inside
 * the application itself - packaged as a classpath resource, the same way
 * {@link AsciiArtRenderer} reads its art - rather than a file on the
 * user's real, separate filesystem.
 */
public final class ClasspathWordRepository implements WordRepository {

    private final String resourceBasePath;
    private final Random random;

    /**
     * @param resourceBasePath classpath folder containing the bundled word
     *                          list files, e.g. {@code "/game-assets/words"}
     * @param random            source of randomness for picking a word
     */
    public ClasspathWordRepository(String resourceBasePath, Random random) {
        Objects.requireNonNull(resourceBasePath, "resourceBasePath must not be null");
        this.random = Objects.requireNonNull(random, "random must not be null");
        if (resourceBasePath.isBlank()) {
            throw new IllegalArgumentException("resourceBasePath must not be blank");
        }
        this.resourceBasePath = resourceBasePath.endsWith("/")
                ? resourceBasePath.substring(0, resourceBasePath.length() - 1)
                : resourceBasePath;
    }

    @Override
    public String getRandomWord(String filename) throws IOException {
        Objects.requireNonNull(filename, "filename must not be null");
        if (filename.isBlank()) {
            throw new IllegalArgumentException("filename must not be blank");
        }

        String resourcePath = resourceBasePath + "/" + filename;
        List<String> rawLines = ClasspathResources.readLines(resourcePath);
        List<String> words = new ArrayList<>();
        for (String line : rawLines) {
            line = line.trim();
            if (!line.isEmpty()) {
                words.add(line.toUpperCase());
            }
        }
        if (words.isEmpty()) {
            throw new IOException("Word list resource contains no words: " + resourcePath);
        }
        return words.get(random.nextInt(words.size()));
    }
}