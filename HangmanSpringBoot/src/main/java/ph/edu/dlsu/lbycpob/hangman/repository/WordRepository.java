package ph.edu.dlsu.lbycpob.hangman.repository;
import java.io.IOException;
/**
 * Something that can hand back a random secret word.
 */
public interface WordRepository {

    /**
     * Returns one random, upper-cased word read from {@code filename}.
     *
     * @throws IOException if the file does not exist, cannot be read, or
     *                      contains no usable words
     */
    String getRandomWord(String filename) throws IOException;
}