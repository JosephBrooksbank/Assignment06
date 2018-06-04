import java.util.Set;
import java.util.HashSet;

public abstract class Hangman {
	// constants
	public static final char BLANK = '-';
	
	// member variables to keep track of a hangman game
	// -- the number of guesses the player has left
	protected int guessesRemaining;
	// -- the length of the secret word
	protected int length;
	// -- the current state of the game
	//    has a blank for each unrevealed location
	//    has a character of the secret word at each revealed location
	protected char[] state;
	// -- stores the guesses made by the user so far
	protected Set<Character> guesses;
	
	// constructor
	public Hangman(int length, int guessCount) {
		// initialize the two integer member variables
		this.guessesRemaining = guessCount;
		this.length = length;
		
		// initialize the state with all blanks
		state = new char[length];
		for(int i = 0; i < state.length; i++) {
			state[i] = BLANK;
		}
		
		// initialize the guesses set to an empty set
		guesses = new HashSet<>();
	}
	
	// methods for checking the state of the game
	public final boolean isGameWon() {
		return (getBlanksRemaining() <= 0);
	}
	
	public final boolean isGameLost() {
		return (getGuessesRemaining() <= 0);
	}
	
	public boolean isGameOver() {
		return isGameWon() || isGameLost();
	}
	
	// various helper getter methods
	public final int getGuessesRemaining() {
		return guessesRemaining;
	}
	
	public final int getBlanksRemaining() {
		int blankCount = 0;
		for(int i = 0; i < state.length; i++) {
			if(state[i] == BLANK) {
				blankCount++;
			}
		}
		return blankCount;
	}
	
	// allowing the user to make a guess
	public final boolean makeGuess(char c) {
		// convert guess to lowercase
		c = Character.toLowerCase(c);
		
		// ensure guess is alphabetic, hasn't been guessed already, and that
		//   the game is not yet over
		if(!Character.isAlphabetic(c) || guesses.contains(c) || isGameOver()) {
			return false;
		}
		
		// add to characters guessed
		guesses.add(c);
		
		// otherwise, try to make the actual guess
		if(makeNewGuess(c)) {
			// guess was successful, just return true
			return true;
		} else {
			// guess was unsuccessful, decrement guesses remaining and return
			//   false
			guessesRemaining--;
			return false;
		}
	}
	
	// printing this object just returns the state
	public String toString() {
		return new String(state);
	}
	
	// what you have to implement
	// -- should update the state using the given character
	protected abstract boolean makeNewGuess(char c);
	// -- should return a word that is possible at the current point
	//    must match all currently revealed characters and cannot contain
	//    letters we've discarded
	public abstract String getWord();
}
