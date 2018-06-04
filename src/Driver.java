import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Driver class for Assignment 06: Malicious Hangman
 *
 * @author Joseph Brooksbank
 * @version 6/3/2018
 *
 * To whoever grades this: Have a good summer!
 */
public class Driver {

    public static void main(String[] args) {

        // Set of all "words" in the dictionary -- I personally would choose a different dictionary, that contains
        // only real english words for easier and more logical play.
        Set<String> dictionary = new HashSet<>();

        try {
            Scanner fileIn = new Scanner(new FileInputStream("dictionary.txt"));
            while (fileIn.hasNext()){
                dictionary.add(fileIn.next());
            }
            fileIn.close();
        } catch (FileNotFoundException e){
            System.out.println("Dictionary not found, can't continue without...");
            System.exit(1);
        }

        Scanner stdin = new Scanner(System.in);
        boolean cont = true;
        while(cont){


            System.out.println("How evil should the computer be? \n 1: Not at all \n 2: Slightly \n 3: Computer should win");
            int evilness = stdin.nextInt();

            System.out.println("Length of word?");
            int wordLength = stdin.nextInt();

            System.out.println("Number of guesses?");
            int guesses = stdin.nextInt();

            Hangman hangman = new StandardHangman(dictionary, wordLength, guesses);

            switch (evilness){
                case 1:
                    System.out.println("Selected normal play. Have fun!");
                    hangman = new StandardHangman(dictionary, wordLength, guesses);
                    break;
                case 2:
                    System.out.println("Selected partial maliciousness. Be careful!");
                    hangman = new PartialMaliciousHangman(dictionary, wordLength, guesses);
                    break;
                case 3:
                    System.out.println("Selected fully malicious computer. Good luck.");
                    hangman = new MaliciousHangman(dictionary, wordLength, guesses);
            }

            System.out.println("DEBUGGING: secret word: " + hangman.getWord());

            while (!hangman.isGameOver()) {
                System.out.println("Guess a letter:");
                String guess = stdin.next();
                if(hangman.makeGuess(guess.charAt(0))){
                    System.out.println("Correct guess!");
                } else {
                    System.out.println("Incorrect guess!");
                }

                System.out.println("Board: " + hangman);
                System.out.println("Number of guesses left: " + hangman.guessesRemaining);
            }

            String endText = hangman.isGameWon() ? "You won!" : "You lost :(";
            System.out.println(endText);

            System.out.println("The word might have been: " + hangman.getWord());
            System.out.println("Do you want to play again? (yes/no)");

            switch(stdin.next()){
                case "yes":
                    cont = true;
                    break;
                case "no":
                    cont = false;
                    break;
                default:
                    System.out.println("Unrecognized input, taking that as a 'no'");
                    cont = false;
                    break;
            }
        }

    }
}
