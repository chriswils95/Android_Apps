package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class EvilHangman {

    public static void main(String args[]) throws IOException, EmptyDictionaryException, GuessAlreadyMadeException {

        File dictionary =  new File(args[0]);
        int wordLength =  Integer.parseInt(args[1]);
        int max_num_of_plays = Integer.parseInt(args[2]);
        EvilHangmanGame hangman = new EvilHangmanGame();
        hangman.setMaxNumOfPlays(max_num_of_plays);
        hangman.startGame(dictionary, wordLength);
    }

}
