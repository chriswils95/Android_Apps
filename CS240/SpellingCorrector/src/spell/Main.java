package spell;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.*;
import java.io.*;



/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {
	
	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	public static void main(String[] args) throws IOException {


		SpellCorrector corrector = new SpellCorrector();
		corrector.useDictionary(args[0]);
		corrector.tempFilename = args[0];
		File inputFile = new File(args[1]);
		Scanner scanner = new Scanner(inputFile);
		String similar_words = corrector.suggestSimilarWord("Jason");
         int a = 0;
		/*while (scanner.hasNext()) {
			String similar_words = corrector.suggestSimilarWord(scanner.next());
			int x = 0;
		}*/
	}
}
