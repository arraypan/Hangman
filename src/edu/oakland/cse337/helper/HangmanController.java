package edu.oakland.cse337.helper;

import edu.oakland.cse337.gui.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.Random;
import java.util.logging.*;
import java.util.stream.Stream;

/**
 * @author Dean DeHart
 * 16/09/09 Due 16/09/14
 * CSE337 Hangman Prerequisite Assignment
 * For updates see: github.com/Deanout/Hangman
 */
public class HangmanController {
    // String that contains the word to be guessed.
    private String answer;
    // Char array that is generated from the answer string
    private char[] display;
    // Initializes a GUI object but does not instantiate it.
    private final HangmanGUI gui;
    private final Random rand = new Random();
    // Keeps the stream open until a word < 16 char long is found.
    private boolean eligibleWordFound = true;
    // Integer that keeps track of the number of guesses.
    private int guessCounter;
    
    // Class constructor
    public HangmanController(HangmanGUI inputGUI) {
        this.gui = inputGUI;
        initializeDisplayText();
        
    }
    // Retrieves a word to be guessed by the user. The requirements for the
    // word are only that it be less than 16 characters long.
    public void generateWord() throws IOException {
        
        while (eligibleWordFound) {
            int randomNum = rand.nextInt(109563 + 1);
            // Stream that finds the randomNum'th line and gets that word.
            try (Stream<String> lines = Files.lines(Paths.get("wordlist.txt"))) {
                answer = lines.skip(randomNum).findFirst().get();
            } 
            // Checks if the chosen word is less than 16 characters. If so,
            // the loop will terminate.
            if (answer.length() < 16) {
                eligibleWordFound = false;
            } else {
            // If the chosen word is greater than 16 characters, nothing happens
            // and the loop repeats.
            }
        }
        // Sets the running condition to true so that if the game is stopped
        // and started again, the loop will run.
        eligibleWordFound = true;
    }
    // This method is called whenever a key is pressed.
    // The input is a letter a-z.
    public void userGuess(char input) {
        /* Checks every letter of the answer and compares it to the input.
         * If they match, the display char string is updated to display that letter,
         * while the rest of the string is either previously guessed letters or
         * underscores. 
         */
        for (int i = 0; i < answer.length(); i++) {
            if (Character.toLowerCase(answer.charAt(i)) == Character.toLowerCase(input)) {
                display[i] = input;
            }
        }
        // Updates the display text with the new display. Note: This will
        // run even if no correct letters were guessed.
        updateDisplayText(display);
        // Increments the counter in the top left of the screen to indicate a guess.
        incrementGuessCounter();
        // Updates the corresponding label.
        gui.setGuessCounterLabel(guessCounter + " tries");
    }
    // Called to initialize the display to all X's. This is used whenever the game
    // is in a 'stop' state, such as when the game is first started.
    public void initializeDisplayText() {
        display = new char[15];
        for (int i = 0; i < display.length; i++) {
            display[i] = 'X';
        }
        updateDisplayText(display);
    }
    // Called whenever the Start Game button is pressed.
    // Initializes the display text to underscores.
    public void newGame() {
        try {
            generateWord();
        } catch (IOException ex) {
            Logger.getLogger(HangmanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Sets the display array to be the same length as the chosen word.
        display = new char[answer.length()];
        for (int i = 0; i < display.length; i++) {
            display[i] = '_';
        }
        updateDisplayText(display);
    }
    /* Called whenever the display text is updated. This is where the spaces are
     * added to the array in order to improve quality of life for the user.
     * @note Previously returned a string.
     */
    public void updateDisplayText(char[] input) {
        char[] constructedInput = new char[input.length * 2];
        for (int i = 0; i < constructedInput.length; i += 2) {
            constructedInput[i] = input[i / 2];
            constructedInput[i + 1] = ' ';
        }
        String text = new String(constructedInput);
        gui.setDisplayText(text);
    }    
    // Called whenever the user attempts a letter.
    public void incrementGuessCounter() {
        guessCounter += 1;
    }
    // Resets the guess counter.
    public void resetCounter() {
        guessCounter = 0;
        gui.setGuessCounterLabel(Integer.toString(guessCounter) + " tries");
    }
}