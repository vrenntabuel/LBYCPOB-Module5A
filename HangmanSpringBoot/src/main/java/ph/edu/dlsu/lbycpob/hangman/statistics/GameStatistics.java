package ph.edu.dlsu.lbycpob.hangman.statistics;

import java.util.Locale;

/**
 * Holds the running statistics for one program session: games played,
 * games won, and the best (highest) number of guesses remaining at the end
 * of any single game.
 */
public record GameStatistics(int gamesPlayed, int gamesWon, int bestGuessesRemaining) {

    /** Runs automatically every time a GameStatistics is created - checks the numbers make sense. */
    public GameStatistics {
        if (gamesPlayed < 0) {
            throw new IllegalArgumentException("gamesPlayed must be >= 0, got " + gamesPlayed);
        }
        if (gamesWon < 0 || gamesWon > gamesPlayed) {
            throw new IllegalArgumentException(
                    "gamesWon must be between 0 and gamesPlayed (" + gamesPlayed + "), got " + gamesWon);
        }
        if (bestGuessesRemaining < 0) {
            throw new IllegalArgumentException(
                    "bestGuessesRemaining must be >= 0, got " + bestGuessesRemaining);
        }
    }

    /** The statistics for a session in which no games have been played yet. */
    public static GameStatistics empty() {
        return new GameStatistics(0, 0, 0);
    }

    /**
     * Returns a <em>new</em> {@code GameStatistics} reflecting one more
     * completed game. This instance is left unchanged.
     */
    public GameStatistics withGame(boolean won, int guessesRemaining) {
        if (guessesRemaining < 0) {
            throw new IllegalArgumentException("guessesRemaining must be >= 0, got " + guessesRemaining);
        }
        int newBest = (gamesPlayed == 0) ? guessesRemaining : Math.max(bestGuessesRemaining, guessesRemaining);
        return new GameStatistics(gamesPlayed + 1, gamesWon + (won ? 1 : 0), newBest);
    }

    /** Percentage of played games that were won, as a value in [0.0, 100.0]. */
    public double winPercentage() {
        return (gamesPlayed == 0) ? 0.0 : (gamesWon * 100.0) / gamesPlayed;
    }

    /** One-decimal-place formatted win percentage, e.g. {@code "50.0%"}. */
    public String formattedWinPercentage() {
        return String.format(Locale.ROOT, "%.2f%%", winPercentage());
    }
}