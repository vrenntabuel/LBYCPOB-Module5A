package ph.edu.dlsu.lbycpob.hangman.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ph.edu.dlsu.lbycpob.hangman.model.GameState;
import ph.edu.dlsu.lbycpob.hangman.service.HangmanService;
import ph.edu.dlsu.lbycpob.hangman.statistics.GameStatistics;
import ph.edu.dlsu.lbycpob.hangman.statistics.StatisticsWriter;

/**
 * HTTP controller – the web equivalent of the {@code Hangman.run()} game
 * loop.
 *
 * <p><b>Request flow</b>
 * <ol>
 *   <li>{@code GET  /}           – welcome / word-list selection page</li>
 *   <li>{@code POST /game/start} – initialise session; pick first secret word</li>
 *   <li>{@code GET  /game/play}  – render current game state (art, hint, keyboard)</li>
 *   <li>{@code POST /game/guess} – process one letter; PRG-redirect back to play</li>
 *   <li>{@code POST /game/again} – keep statistics, start a fresh round</li>
 *   <li>{@code GET  /game/stats} – display session statistics, write to file,
 *                                  invalidate session</li>
 *   <li>{@code GET  /game/reset} – abandon session, return to welcome page</li>
 * </ol>
 *
 */
@Controller
public class GameController {

    private static final String SESSION_KEY = "gameState";

    private final HangmanService    hangmanService;
    private final StatisticsWriter  statisticsWriter;

    public GameController(HangmanService hangmanService,
                          StatisticsWriter statisticsWriter) {
        this.hangmanService   = hangmanService;
        this.statisticsWriter = statisticsWriter;
    }

    // ------------------------------------------------------------------ //
    //  Welcome page                                                         //
    // ------------------------------------------------------------------ //

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ------------------------------------------------------------------ //
    //  Start a new session                                                  //
    // ------------------------------------------------------------------ //

    /**
     * Initializes the session with a fresh {@link GameState} and picks the
     * first secret word. Mirrors the filename-prompt and first
     * {@code getRandomWord} call that opened {@code Hangman.run()}.
     */
    @PostMapping("/game/start")
    public String startGame(@RequestParam("filename") String filename,
                            HttpSession session) {
        GameState state = new GameState();
        state.setFilename(filename.trim());

        String word = hangmanService.getRandomWord(state.getFilename());
        state.setSecretWord(word);
        state.setGuessesRemaining(HangmanService.MAX_GUESSES);
        state.setMessage("A new word has been chosen. It has "
                + word.length() + " letter(s). Good luck!");

        session.setAttribute(SESSION_KEY, state);
        return "redirect:/game/play";
    }

    // ------------------------------------------------------------------ //
    //  Display the current game state                                       //
    // ------------------------------------------------------------------ //

    /**
     * Populates the model for the play template. No state is mutated here
     * (GET should be idempotent); all mutation happens in the POST handlers.
     */
    @GetMapping("/game/play")
    public String play(HttpSession session, Model model) {
        GameState state = (GameState) session.getAttribute(SESSION_KEY);
        if (state == null) {
            // Session expired or player navigated here directly – send them home.
            return "redirect:/";
        }

        String hint        = hangmanService.createHint(state.getSecretWord(), state.getGuessedLetters());
        String displayHint = hangmanService.formatHintForDisplay(hint);
        String art         = hangmanService.getHangmanArtAsString(state.getGuessesRemaining());

        model.addAttribute("state",       state);
        model.addAttribute("hint",        hint);
        model.addAttribute("displayHint", displayHint);
        model.addAttribute("hangmanArt",  art);
        model.addAttribute("alphabet",    hangmanService.getAlphabet());
        return "play";
    }

    // ------------------------------------------------------------------ //
    //  Process one letter guess                                             //
    // ------------------------------------------------------------------ //

    /**
     * The heart of the web migration: everything that used to happen inside
     * the {@code while(true)} loop of {@code Hangman.playOneGame()} now
     * happens in this single POST handler, then the player is redirected
     * back to the GET so the browser's history contains a safe, refreshable
     * URL.
     */
    @PostMapping("/game/guess")
    public String guess(@RequestParam("letter") String letterInput,
                        HttpSession session) {

        GameState state = (GameState) session.getAttribute(SESSION_KEY);
        if (state == null || state.isGameOver()) {
            return "redirect:/game/play";
        }

        // --- Input validation (replaces Hangman.readGuess validation) ---
        String cleaned = letterInput.trim().toUpperCase();
        if (cleaned.length() != 1
                || cleaned.charAt(0) < 'A'
                || cleaned.charAt(0) > 'Z') {
            state.setMessage("Please enter a single letter from A to Z.");
            session.setAttribute(SESSION_KEY, state);
            return "redirect:/game/play";
        }

        char letter = cleaned.charAt(0);
        if (state.getGuessedLetters().indexOf(letter) >= 0) {
            state.setMessage("You already guessed \"" + letter
                    + "\". Choose a different letter.");
            session.setAttribute(SESSION_KEY, state);
            return "redirect:/game/play";
        }

        // --- Record the guess ---
        state.setGuessedLetters(state.getGuessedLetters() + letter);

        // --- Evaluate correctness ---
        if (state.getSecretWord().indexOf(letter) >= 0) {
            // Correct guess
            String hint = hangmanService.createHint(
                    state.getSecretWord(), state.getGuessedLetters());

            if (!hint.contains("-")) {
                // All letters revealed – player wins
                state.setGameOver(true);
                state.setWon(true);
                state.setStatistics(
                        state.getStatistics().withGame(true, state.getGuessesRemaining()));
                state.setMessage("You win! The word was \""
                        + state.getSecretWord() + "\". "
                        + state.getGuessesRemaining() + " guess(es) remaining.");
            } else {
                state.setMessage("Correct! \"" + letter + "\" is in the word.");
            }
        } else {
            // Incorrect guess
            state.setGuessesRemaining(state.getGuessesRemaining() - 1);

            if (state.getGuessesRemaining() == 0) {
                // No guesses left – player loses
                state.setGameOver(true);
                state.setWon(false);
                state.setStatistics(state.getStatistics().withGame(false, 0));
                state.setMessage("You lose. The word was \""
                        + state.getSecretWord() + "\".");
            } else {
                state.setMessage("Incorrect! \"" + letter
                        + "\" is not in the word. "
                        + state.getGuessesRemaining() + " guess(es) left.");
            }
        }

        session.setAttribute(SESSION_KEY, state);
        return "redirect:/game/play";
    }

    // ------------------------------------------------------------------ //
    //  Play another round (mirrors the "Play again? Y/N" prompt)           //
    // ------------------------------------------------------------------ //

    /**
     * Resets game fields but preserves the accumulated {@link GameStatistics},
     * exactly as {@code Hangman.run()}'s {@code while (playAgain)} loop did.
     */
    @PostMapping("/game/again")
    public String playAgain(HttpSession session) {
        GameState old = (GameState) session.getAttribute(SESSION_KEY);
        if (old == null) {
            return "redirect:/";
        }

        GameState fresh = new GameState();
        fresh.setFilename(old.getFilename());
        fresh.setStatistics(old.getStatistics());   // carry over running totals

        String word = hangmanService.getRandomWord(old.getFilename());
        fresh.setSecretWord(word);
        fresh.setGuessesRemaining(HangmanService.MAX_GUESSES);
        fresh.setMessage("New round! The word has "
                + word.length() + " letter(s). Good luck!");

        session.setAttribute(SESSION_KEY, fresh);
        return "redirect:/game/play";
    }


    // ------------------------------------------------------------------ //
    //  View statistics and end session                                      //
    // ------------------------------------------------------------------ //

    /**
     * Mirrors {@code Hangman.stats()} plus the call to
     * {@code StatisticsWriter.writeStats()}. Session is invalidated after
     * writing so the next visit to {@code /} starts clean.
     */
    @GetMapping("/game/stats")
    public String stats(HttpSession session, Model model) {
        GameState state = (GameState) session.getAttribute(SESSION_KEY);
        if (state == null || state.getStatistics().gamesPlayed() == 0) {
            return "redirect:/";
        }

        GameStatistics s = state.getStatistics();
        model.addAttribute("stats", s);

        statisticsWriter.writeStats(
                s.gamesPlayed(),
                s.gamesWon(),
                s.gamesPlayed() - s.gamesWon(),
                s.winPercentage(),
                s.bestGuessesRemaining());

        session.invalidate();
        return "stats";
    }

    // ------------------------------------------------------------------ //
    //  Abandon session                                                      //
    // ------------------------------------------------------------------ //

    @GetMapping("/game/reset")
    public String reset(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}