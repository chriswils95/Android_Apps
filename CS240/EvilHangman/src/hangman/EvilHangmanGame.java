package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class EvilHangmanGame implements IEvilHangmanGame{

    SortedSet<String> dictionaryWords;
    SortedSet<String> guessWordsWithGivenLength;
    SortedMap<String, Integer> guessWordsWithGivenChars;
    List<Map<String,Integer>> maps;
    List<Integer> listOfWordsLength;
    Map<String, Integer> mapOfMaxLength;
    SortedSet<Character> guessCharacters;
    boolean containsGuessCharacter;
    int guessWordLength;
    int max_num_of_plays;
    SortedMap<String, Integer> guessLetterPositions;
    Map<String, List<Integer>> wordsCharPositions;

    public EvilHangmanGame(){
        dictionaryWords = new TreeSet<>();
        guessWordsWithGivenLength = new TreeSet<>();
        guessWordsWithGivenChars = new TreeMap<>();
        maps = new ArrayList<Map<String,Integer>>();
        listOfWordsLength = new ArrayList<>();
        mapOfMaxLength = new TreeMap<>();
        guessCharacters = new TreeSet<>();
        guessWordLength = 0;
        max_num_of_plays = 0;
        containsGuessCharacter = false;
        wordsCharPositions = new TreeMap<>();
    }

    public void setMaxNumOfPlays(int max){
        max_num_of_plays = max;
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
              dictionaryWords.clear();
               guessWordLength = wordLength;
               Scanner scanner = new Scanner(dictionary);
               if(dictionary.length() == 0){
                   throw new EmptyDictionaryException();
               }
               while (scanner.hasNext()) {
                   dictionaryWords.add(scanner.next());
               }
               int count = 0;
               findWordsWithGivenLength();
               if(guessWordsWithGivenChars.isEmpty()){
                   throw new EmptyDictionaryException();
               }

             String printLine = "";
             char guessCharacter = 'x';
             boolean win = false;
            for(int i = 0; i < wordLength;i++) {
             printLine += "-";
             }
             while (max_num_of_plays != 0) {
                   if(guessWordsWithGivenLength.contains(printLine)){
                       win = true;
                       System.out.println("You won");
                       break;
                   }
                   System.out.println("You have " + max_num_of_plays + " guesses left");
                   Scanner userInput = new Scanner(System.in);
                   System.out.println(toString());
                   System.out.print("Word: ");
                   String testWord = "";
                   if(guessWordsWithGivenLength.size() != 0) {
                       testWord = guessWordsWithGivenLength.first();
                   }
                   if(testWord.length() != 0){
                       StringBuilder testString = new StringBuilder(printLine);
                       for(int i = 0; i < testWord.length();i++){
                           if(testWord.charAt(i) == guessCharacter){
                               testString.setCharAt(i, guessCharacter);
                           }
                       }
                       printLine = testString.toString();
                   }
                   System.out.print(printLine);
                 System.out.print("\nEnter guess: ");
                   while (userInput.hasNext()) {
                       try {
                           containsGuessCharacter = false;
                           String input = userInput.next();
                           if(input.length() > 1) {
                               System.out.println("Please enter a character not a string\n");
                            }
                           else {
                               char c = input.charAt(0);
                               if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                                   try {
                                       guessCharacter = c;
                                       makeGuess(c);
                                       max_num_of_plays--;
                                   }catch (Exception e){
                                       System.out.println("word already guess");
                                   }
                               }else{
                                  System.out.println("please enter a valid character (a-z");
                               }
                           }
                       }
                       catch (Throwable e){

                       }
                       count++;
                       break;
                   }
               }

             if(!win){
                 if(!guessWordsWithGivenLength.isEmpty()) {
                     System.out.println("You lose the word is " + guessWordsWithGivenLength.first());
                 }
             }
               guessCharacters.clear();

    }
    public void StoresWordsPositions(char guess){
        wordsCharPositions.clear();
        for (Map.Entry<String,Integer> entry : guessWordsWithGivenChars.entrySet()) {
            List<Integer> tempList = new ArrayList<>();
            String temp = entry.getKey();
            for(int i = 0; i < temp.length(); i++){
                if(temp.charAt(i) == guess){
                    tempList.add(i);
                }
            }
            if(tempList.isEmpty()){
                tempList.add(-1);
            }
            wordsCharPositions.put(temp, tempList);
        }
    }

   public void findWordsWithGivenLength(){
        guessWordsWithGivenChars.clear();
        Iterator it = dictionaryWords.iterator();
        while(it.hasNext()){
            String word = (String) it.next();
            if(word.length() == guessWordLength){
              guessWordsWithGivenChars.put(word, guessWordLength);
            }
        }
    }

    public void storeWordsWithGivenChar(char guess){
        listOfWordsLength.clear();
        for (Map.Entry<String,Integer> entry : guessWordsWithGivenChars.entrySet()){
            int test = entry.getValue();
            for(int i = 0; i < entry.getKey().length();i++) {
                if (entry.getKey().charAt(i) == guess){
                    test--;
                }
            }
            listOfWordsLength.add(test);
            guessWordsWithGivenChars.replace(entry.getKey(), test);
        }
    }

    public void setSortedMapOfStrings(){
        SortedMap<String, Integer> temp = new TreeMap<>();
        Iterator it = guessWordsWithGivenLength.iterator();
        while(it.hasNext()){
            for (Map.Entry<String,Integer> entry : guessWordsWithGivenChars.entrySet()) {
                if(entry.getKey() == it.next()){
                    temp.put(entry.getKey(), entry.getValue());
                }
            }
        }
        guessWordsWithGivenChars.clear();
        guessWordsWithGivenChars = temp;
    }

    public void setSortedSetOfStrings(){
        guessWordsWithGivenLength.clear();
        for (Map.Entry<String,Integer> entry : guessWordsWithGivenChars.entrySet()) {
            guessWordsWithGivenLength.add(entry.getKey());
        }
    }

    public boolean isSetContainsGuessCharacter(char guess){
        Iterator it = guessCharacters.iterator();
        while(it.hasNext()){
            if((char)it.next() == guess){
                return true;
            }
        }
        return false;
    }

    public boolean findMapWithSameLength() {
     Iterator it = maps.iterator();
        List<Integer> testList = new ArrayList<>();
        while(it.hasNext()){
            Map<String, Integer> test = (Map<String, Integer>) it.next();
            testList.add(test.size());
         }
       int counter = 0;
        int max = Collections.max(testList);
        for(int i = 0; i < testList.size();i++) {
            if(testList.get(i) == max){
                counter++;
            }
        }

        if(counter > 1){
            return true;
        }

        return false;
    }

    public SortedSet<String> MapWithMaxSize(){
        Iterator it = maps.iterator();
        Map<String, Integer> temp = (Map<String, Integer>)it.next();
        while(it.hasNext()) {
            Map<String, Integer> test = (Map<String, Integer>)it.next();
            if (test.size() > temp.size()) {
                temp = test;
            }
        }
        guessWordsWithGivenLength.clear();
        guessWordsWithGivenChars.clear();
        for (Map.Entry<String,Integer> entry : temp.entrySet()) {
            guessWordsWithGivenChars.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String,Integer> entry : temp.entrySet()) {
            guessWordsWithGivenLength.add(entry.getKey());
        }
        return guessWordsWithGivenLength;
    }

    public void findWordsWithGuessLetter(char guess){
            Map<String, Integer> temp = new TreeMap<>();
            for (Map.Entry<String,Integer> entry : guessWordsWithGivenChars.entrySet()) {
                Boolean test = false;
                for(int i = 0; i < entry.getKey().length();i++) {
                    if (entry.getKey().charAt(i) == guess) {
                        test = true;
                    }
                }
                if(!test){
                    temp.put(entry.getKey(), entry.getValue());
                }
        }

            if(!temp.isEmpty()){
                maps.add(temp);
            }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        List<Map<String, List<Integer>>> t1 = new ArrayList<>();
        StoresWordsPositions(guess);
        if(Character.isUpperCase(guess)){
           guess = Character.toLowerCase(guess);
        }
        if(isSetContainsGuessCharacter(guess)){
            throw new GuessAlreadyMadeException();
        }

        int temp = 0;
        maps.clear();
        guessCharacters.add(guess);
        storeWordsWithGivenChar(guess);
        for (Map.Entry<String,List<Integer>> entry : wordsCharPositions.entrySet()) {
            Map<String, List<Integer>> test = (wordsCharPositions.entrySet().stream()
                    .filter(map -> map.getValue().equals(entry.getValue())).collect(Collectors.toMap(p ->p.getKey(), p-> p.getValue())));
            t1.add(test);
        }

        Iterator it = t1.iterator();
        List<Map<String,List<Integer>>> t3 = new ArrayList<>();
        while(it.hasNext()){
            Map<String, List<Integer>> t2 = (Map<String, List<Integer>>) it.next();
            if(!t3.contains(t2)){
                t3.add(t2);
            }
        }

        Iterator its = t3.iterator();
        while(its.hasNext()){
            Map<String, Integer> t6 = new TreeMap<>();
            Map<String, List<Integer>> t4 = (Map<String, List<Integer>>) its.next();
            for (Map.Entry<String,List<Integer>> entry : t4.entrySet()) {
                for (Map.Entry<String, Integer> entrys : guessWordsWithGivenChars.entrySet()) {
                    if(entry.getKey().equals(entrys.getKey())){
                        t6.put(entrys.getKey(), entrys.getValue());
                    }
                }
            }
            maps.add(t6);
        }




        //findWordsWithGuessLetter(guess);

         if(findMapWithSameLength()){
             setNewGuessStringsSet(guess);
             setSortedSetOfStrings();
             return guessWordsWithGivenLength;
         }

         guessWordsWithGivenLength.clear();
        guessWordsWithGivenLength = MapWithMaxSize();
        return guessWordsWithGivenLength;

    }

    public void setNewGuessStringsSet(char guess){
        guessWordsWithGivenChars.clear();
        mapOfMaxLength.clear();
        Iterator it = maps.iterator();
        int max = Collections.max(listOfWordsLength);
        while(it.hasNext()) {
            Map<String, Integer> temp = (Map<String, Integer>) it.next();
            for (Map.Entry<String,Integer> entry : temp.entrySet()) {
                mapOfMaxLength.put(entry.getKey(), entry.getValue());
            }
        }

        mapOfMaxLength = mapOfMaxLength.entrySet().stream()
                .filter(map -> map.getValue() == max)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        setContainsGuessCharacter(guess) ;
        //containsGuessCharacter = false;
       if(containsGuessCharacter) {
           filterMaxLengthWords(guess);
        }

        for (Map.Entry<String,Integer> entry : mapOfMaxLength.entrySet()) {
            guessWordsWithGivenChars.put(entry.getKey(), entry.getValue());
        }
    }

    public void filterMaxLengthWords(char guess){
        int counter = guessWordLength - 1;
        while(counter != 0){
            Map<String, Integer> temp = new TreeMap<>();

            for (Map.Entry<String,Integer> entry : mapOfMaxLength.entrySet()) {
                if(entry.getKey().charAt(counter) == guess){
                  temp.put(entry.getKey(), entry.getValue());
                }
            }
            if(!temp.isEmpty() &&(mapOfMaxLength.size() != temp.size())){
                mapOfMaxLength.clear();
                mapOfMaxLength = temp;
                break;
            }
            counter--;
        }

    }

    public void setContainsGuessCharacter(char guess){
        for (Map.Entry<String,Integer> entry : mapOfMaxLength.entrySet()) {
            for(int i = 0; i < entry.getKey().length();i++) {
                if (entry.getKey().charAt(i) == guess){
                    containsGuessCharacter = true;
                }
            }
        }
    }

    public String toString(){
        String str = "Used Letters: ";
        Iterator it = guessCharacters.iterator();
        while (it.hasNext()) {
            str += (Character)it.next();
        }
        return str;
    }



    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessCharacters;
    }


}
