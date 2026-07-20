package ph.edu.dlsu.lbycpob.hangman.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Writes Hangman session statistics to a flat file.
 */
@Component
public class StatisticsWriter {

    private static final Logger log = LoggerFactory.getLogger(StatisticsWriter.class);

    private static final String FILENAME  = "hangman_statistics.txt";
    private static final String SEPARATOR = "=".repeat(60);

    public void writeStats(int gamesPlayed, int gamesWon, int gamesLost,
                           double winPercentage, int bestScore) {
        try {
            ensureFileExists();
            appendStatsToFile(gamesPlayed, gamesWon, gamesLost, winPercentage, bestScore);
            log.info("Session statistics saved to {}", FILENAME);
        } catch (IOException e) {
            log.error("Error writing statistics to file: {}", e.getMessage(), e);
        }
    }

    private void appendStatsToFile(int gamesPlayed, int gamesWon, int gamesLost,
                                   double winPercentage, int bestScore) throws IOException {
        try (FileWriter fw = new FileWriter(FILENAME, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(SEPARATOR);
            bw.newLine();
            bw.write("Hangman Game Session - " + getCurrentTimestamp());
            bw.newLine();
            bw.write(SEPARATOR);
            bw.newLine();
            bw.write(String.format("Total Games Played:  %d%n", gamesPlayed));
            bw.write(String.format("Games Won:           %d%n", gamesWon));
            bw.write(String.format("Games Lost:          %d%n", gamesLost));
            bw.write(String.format("Win Percentage:      %.1f%%%n", winPercentage));
            bw.write(String.format("Best Score:          %d guess(es) remaining%n", bestScore));
            bw.newLine();
        }
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private void ensureFileExists() throws IOException {
        File file = new File(FILENAME);
        if (!file.exists()) {
            try (FileWriter fw = new FileWriter(FILENAME)) {
                fw.write("HANGMAN GAME STATISTICS LOG\n");
                fw.write("Session records appended below\n\n");
            }
        }
    }
}