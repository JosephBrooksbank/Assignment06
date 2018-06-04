import java.util.*;

public class StandardHangman extends Hangman{

    String secretWord;

    /**
     * Constructor for "Standard" hangman gameplay
     * @param dictionary    The set of all possible words to play with -- note, I don't personally agree with this list of
     *                      "words", because it contains a fair number of strings which don't actually contain vowels
     *                      and appear to simply be combinations of random letters
     * @param length        The length of the word, as chosen by the user
     * @param guesses       The number of guesses, as chosen by the user
     */
    StandardHangman(Set<String> dictionary, int length, int guesses){
        // Setting up the basic hangman structure
        super(length, guesses);

        // A list of all possible words of the supplied length. Using list > set because of the easy of randomization
        List<String> possibleWords = new ArrayList<>();
        for (String aWord : dictionary){
            if (aWord.length() == length){
                possibleWords.add(aWord);
            }
        }
        Random rand = new Random();
        // Getting a word at a random index to be the word to guess
        secretWord = possibleWords.get(rand.nextInt(possibleWords.size()));

    }


    /**
     * Method to handle guesses in an honest environment
     */
    @Override
    protected boolean makeNewGuess(char c) {
        // Using an index based iteration to handle every position of the character in the word
        int index = secretWord.indexOf(c);
        // if the character exists in the word, check for every index
        if (secretWord.indexOf(c) != -1){
            while (index >= 0){
                this.state[index] = c;
                // check the rest of the word after the index found
                index = secretWord.indexOf(c, index +1);
            }
            return true;
        }
        return false;
    }


    /**
     * Method to return possible word the user was guessing. This is honest hangman, so the word is the same the entire
     * game.
     */
    @Override
    public String getWord() {
        return secretWord;
    }
}
