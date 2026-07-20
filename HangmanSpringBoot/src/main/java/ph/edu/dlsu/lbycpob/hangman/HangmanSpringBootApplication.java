package ph.edu.dlsu.lbycpob.hangman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ph.edu.dlsu.lbycpob.hangman.render.AsciiArtRenderer;
import ph.edu.dlsu.lbycpob.hangman.render.HangmanRenderer;
import ph.edu.dlsu.lbycpob.hangman.repository.ClasspathWordRepository;
import ph.edu.dlsu.lbycpob.hangman.repository.WordRepository;

import java.util.Random;

@SpringBootApplication
public class HangmanSpringBootApplication {

    /**
     * Shared classpath root - mirrors the original {@code GAME_ASSETS_BASE_PATH}.
     */
    private static final String GAME_ASSETS_BASE_PATH = "/game-assets";

    public static void main(String[] args) {
        SpringApplication.run(HangmanSpringBootApplication.class, args);
    }

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public WordRepository wordRepository(Random random) {
        return new ClasspathWordRepository(
                GAME_ASSETS_BASE_PATH + "/words",
                random
        );
    }

    @Bean
    public HangmanRenderer hangmanRenderer() {
        return new AsciiArtRenderer(
                GAME_ASSETS_BASE_PATH + "/hangman-art"
        );
    }
}