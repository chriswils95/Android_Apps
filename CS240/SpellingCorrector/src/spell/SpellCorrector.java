package spell;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Scanner;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {

    Trie test = new Trie();
    String tempFilename;





    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File inputFile = new File(dictionaryFileName);
        Scanner scanner = new Scanner(inputFile);
        while(scanner.hasNext()){
            test.add(scanner.next());
        }
        scanner.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        SortedMap<String,Integer> edit_words = new TreeMap<String, Integer>();;
        INode findNode = test.find(inputWord);
        if(inputWord.isEmpty()){
            return null;
        }

        if(findNode != null){

            StringBuilder temp = new StringBuilder(inputWord);
            for(int i = 0; i < temp.length();i++){
                char a = temp.charAt(i);
                if(Character.isUpperCase(a)) {
                    temp.setCharAt(i, Character.toLowerCase(a));
                }
            }

            inputWord = temp.toString();
            return inputWord;
        }

        if(test.deletionDistance(inputWord) != null){
            INode deletedNode = test.find(test.deletionDistance(inputWord));
              edit_words.put(test.deletionDistance(inputWord), deletedNode.getValue());
        }

        if(test.insertionDistance(inputWord) != null){
            INode insertedNode = test.find(test.insertionDistance(inputWord));
            edit_words.put(test.insertionDistance(inputWord), insertedNode.getValue());
        }

        if(test.alterationDistance(inputWord) != null){
            INode alternateNode = test.find(test.alterationDistance(inputWord));
            edit_words.put(test.alterationDistance(inputWord), alternateNode.getValue());
        }

        if(test.tranpositionDistance(inputWord) != null){
            INode transposeNode = test.find(test.tranpositionDistance(inputWord));
            edit_words.put(test.tranpositionDistance(inputWord), transposeNode.getValue());
        }

        if(edit_words.isEmpty()){
           String edited = test.second_edit();
           if(edited == null){
               return null;
           }else{
               return edited;
           }

        }

        return test.findMax(edit_words);
    }
    }



