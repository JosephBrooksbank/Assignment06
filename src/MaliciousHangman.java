import java.util.*;

public class MaliciousHangman extends Hangman {

    /** A set containing all possible words for the computer to draw from for this round of guesses */
    private Set<String> possibleWords;

    /**
     * Constructor for a completely malicious game of Hangman. Plays as unbeatable as possible, maintaining the largest
     * amount of possibilities for the player to continue guessing from.
     * @param dictionary    The initial set of words able to be drawn from
     * @param length        The length of the word as set by the player
     * @param guesses       The number of guesses as set by the player 
     */
    MaliciousHangman(Set<String> dictionary, int length, int guesses){
        super(length, guesses);

        // Set of all possible words that the
        possibleWords = new HashSet<>();
        for (String aWord : dictionary){
            if (aWord.length() == length)
                possibleWords.add(aWord);
        }

    }

    /**
     * Method to handle guesses in a completely malicious environment
     * @param c     The character that was guessed
     * @return      Whether or not the player guessed correctly (read> the computer 'let' the player win)
     */
    @Override
    protected boolean makeNewGuess(char c) {
        // A map to contain all possibilities of guessing char c.
        // Mapping of [state with added letters] -> [Set of words that achieve this state]
        Map<String, Set<String>> possibilities = new HashMap<>();

        // Iterates through every possible word
        for (String aWord : possibleWords){

            // Making a copy of the current state, that will be modified for each possibility
            char[] tempState = this.state.clone();

            // creating a state based on adding this character to a possible word -- NOTE you've probably seen this code
            // all around my project (for each implementation of Hangman), I'd normally have this be a method in
            // the abstract Hangman class, but the instructions specifically said NOT to modify that class at all.
            int index = aWord.indexOf(c);
            while (index >= 0){
                tempState[index] = c;
                index = aWord.indexOf(c, index+1);
            }

            // Converting to String for the key -- strings are immutable, which is really helpful when comparing keys
            String keyValue = new String(tempState);

            // If the map already contains this key, just add the word to the set
            if (possibilities.containsKey(keyValue)){
                possibilities.get(keyValue).add(aWord);
            }

            // otherwise, create a new mapping with a new set
            else {
                // There isn't a way that I've found to efficiently one-line construct a set with values, one day
                // I'll find a way
                Set<String> tempSet = new HashSet<>();
                tempSet.add(aWord);
                possibilities.put(keyValue, tempSet);
            }
        }

        // Finding which decision is the best, based on the number of possibilities in the set
        // stores the best state in a separate object, to be compared later
        char[] bestState = this.state;

        int maxSize = Integer.MIN_VALUE;
        for (Map.Entry<String, Set<String>> aPair : possibilities.entrySet()){

            if (aPair.getValue().size() > maxSize){

                // state is held in a char array, so bestState is converted to a char array for comparison
                bestState = aPair.getKey().toCharArray();
                // Setting the possible words for next guess to the best possibility
                this.possibleWords = aPair.getValue();
                // Setting max size to new value
                maxSize = aPair.getValue().size();
            }
        }

        // checking to see if the board state changed, if the computer "let" the player win a round
        boolean isChanged = !Arrays.equals(state, bestState);
        state = bestState;
        return isChanged;

    }

    /**
     * Method to get a possible word that the user was guessing. In the case of Partially Malicious hangman,
     * the word could potentially be any number of words that don't include letters guessed so far. Thus,
     * it just chooses a random one from the set of potential words.
     */
    @Override
    public String getWord() {

        // otherwise, just some word from the set of potential words.
        return getRandomFromPossibleWords();
    }

    /**
     * Private method to get a "random" value from a set.
     * NOTE: Same code used in partially malicious hangman. Again, this would normally be in the abstract Hangman,
     * but am not allowed to edit that.
     * @return  The random value from the set
     */
    private String getRandomFromPossibleWords(){
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
                System.out.println("DEBUGGING: Secret word is " + aWord);
                break;
            }
            i++;
        }
        return ret;
    }
}
