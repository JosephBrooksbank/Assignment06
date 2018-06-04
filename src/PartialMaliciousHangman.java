import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Partially Malicious Hangman gameplay. Computer will try to give no letters for as long as possible, before eventually
 * running out of options and switching to standard.
 *
 * @author Joseph Brooksbank
 * @version 6/3/2018
 * <p>
 * To whoever grades this: Have a good summer!
 */
public class PartialMaliciousHangman extends Hangman {

    /* A set of all possible words that the computer might have selected in a malicious sense */
    // Possible words is now defined in abstract Hangman, so "cheating" mode works... is simple to change back, let me know if I should
    /**
     * A boolean to control whether the computer is playing honestly or not yet (if it has run out of words to remove)
     */
    private boolean isHonest;
    /**
     * Once the computer is playing honestly, this is the word that it uses
     */
    private String secretWord;


    /**
     * Constructor for Partially Malicious Hangman game
     *
     * @param dictionary The set of all potential words for this game
     * @param length     The length of the word that the user selects
     * @param guesses    The number of guesses the user allows themselves
     */
    PartialMaliciousHangman(Set<String> dictionary, int length, int guesses) {
        // Creating a hangman object with the correct length and number of guesses
        super(length, guesses);

        // The computer starts maliciously -- with the way this class is currently built, "Standard" and "Partial Malicious
        // hangman could actually be one class, with one additional argument to determine if the game should play honestly
        // or not
        isHonest = false;

        // Creating the set of potential words for this game, the only limitation at the start is length
        possibleWords = new HashSet<>();
        for (String aWord : dictionary) {
            if (aWord.length() == length)
                possibleWords.add(aWord);
        }
    }


    /**
     * Method to handle guesses in a partially malicious environment
     */
    @Override
    protected boolean makeNewGuess(char c) {
        // if the game is playing honestly, make an honest guess
        if (isHonest) {
            return honestGuess(c);
        } else {
            // checking if there are still words that are able to be removed
            boolean stillWords = false;
            for (String aWord : possibleWords) {
                if (aWord.indexOf(c) == -1) {
                    // There are still words remaining that don't include the guessed character
                    stillWords = true;
                }
            }

            // If there are still words remaining that don't include the character, remove all the ones that do
            if (stillWords) {
                // Java won't let you remove from a set while iterating over it (for obvious reasons) so I'm using a
                // temp set to hold the values to remove
                Set<String> removeList = new HashSet<>();

                for (String aWord : possibleWords) {
                    if (aWord.indexOf(c) != -1) {
                        // The word contains that character
                        removeList.add(aWord);
                    }
                }

                // Can't remove while iterating over a list, so remove all after
                for (String aWord : removeList) {
                    possibleWords.remove(aWord);
                }
                return false;

                // Otherwise, there are no words remaining that the computer can "fool" the user with. Has to play
                // honestly now.
            } else {
                // Grabbing a random value from the set for the new secret word
                secretWord = getRandomFromPossibleWords();
                // Setting the computer to play honestly
                isHonest = true;
                // This will return true, but I'm using this to correctly set the game state anyways
                return honestGuess(c);
            }
        }
    }

    /**
     * Private method to make a guess in an honest fashion, similar to standard hangman
     *
     * @param c The character to guess
     * @return Whether or not the character was in the word
     */
    private boolean honestGuess(char c) {
        // Using an index based iteration to handle all positions of the character in the word
        int index = secretWord.indexOf(c);
        // If the character exists, check for all possible locations in the word
        if (secretWord.indexOf(c) != -1) {
            while (index >= 0) {
                this.state[index] = c;
                index = secretWord.indexOf(c, index + 1);
            }
            return true;
        }
        return false;
    }

    /**
     * Private method to get a "random" value from a set.
     *
     * @return The random value from the set
     */
    private String getRandomFromPossibleWords() {
        Random rand = new Random();
        String ret = "";
        // Getting random "index" of value
        int indexOfWord = rand.nextInt(possibleWords.size());
        int i = 0;
        // Moving through set until that random "index" is hit
        for (String aWord : possibleWords) {
            if (i == indexOfWord) {
                // getting value at that "index"
                ret = aWord;
                break;
            }
            i++;
        }
        return ret;
    }


    /**
     * Method to get a possible word that the user was guessing. In the case of Partially Malicious hangman,
     * the word could potentially be any number of words that don't include letters guessed so far. Thus,
     * it just chooses a random one from the set of potential words.
     */
    @Override
    public String getWord() {

        // If the user made it far enough into the game to force the computer to play honestly, it returns the word
        if (isHonest) return secretWord;
        // otherwise, just some word from the set of potential words.
        return getRandomFromPossibleWords();
    }
}
