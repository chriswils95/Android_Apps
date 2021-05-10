package spell;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.ArrayList; // import the ArrayList class



public class Trie implements ITrie {
    String name;
    private Node root;
    private int num_of_words;
    private int num_of_nodes;
    String return_string;
    StringBuilder steady;
    int t;
    boolean endRecursion;
    ArrayList<String> cars = new ArrayList<String>(); // Create an ArrayList object
    int insertedIndex;
    int alternate_index;
    String [] arrayOfDeletedCharacters;
    String [] arraysOfInsertedCharacters;
    String [] arrayOfAlternateCharacters;
    String [] arrayOfTransposeWords;
    Map<String,Integer> transpose_distance;
    Map<String,Integer> map_of_inserted_chars;
    Map<String,Integer> map_of_alternate_chars;
    SortedMap<String, Integer> dictionaryWords;
    public Trie() {
        root = new Node();
        num_of_words = 0;
        num_of_nodes = 1;
        insertedIndex = 0;
        t = 0;
        alternate_index = 0;
        return_string = "";
        transpose_distance = new HashMap();
        map_of_inserted_chars = new HashMap();
        map_of_alternate_chars = new HashMap();
        steady = new StringBuilder();
        endRecursion = false;
        dictionaryWords = new TreeMap<>();


    }

    public Trie(String name) {
        this.name = name;
    }

    @Override
    public void add(String word) {
        name = word;
        Node currentNode = root;
        int tempWordCount = 0;
        dictionaryWords.put(word, t);
        cars.clear();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';
            if(currentNode.arrayList[index] == null) {
                num_of_nodes++;
                Node temp = new Node();
                currentNode.arrayList[index] = temp;
                currentNode.characterValue = c;
                ///System.out.println(currentNode.arrayList[index].characterValue);
                currentNode = temp;
                tempWordCount++;
            }else{
                tempWordCount++;
                currentNode = currentNode.arrayList[index];
            }
        }
        if((tempWordCount == word.length()) && !currentNode.getIsWordComplete()){
            num_of_words++;
        }
        else{
            currentNode.num_of_words++;
        }
        t++;
        currentNode.setWordComplete(true);
    }

    @Override
    public INode find(String word) {
        Node findNode = root;
        StringBuilder temp = new StringBuilder(word);
        for(int i = 0; i < temp.length();i++){
            char a = temp.charAt(i);
            if(Character.isUpperCase(a)) {
                temp.setCharAt(i, Character.toLowerCase(a));
            }
        }

        word = temp.toString();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';
            if ((findNode.arrayList[index] != null)) {
                //System.out.println(findNode.arrayList[index].characterValue);
                findNode = findNode.arrayList[index];
            } else {
                return null;
            }
        }

        if (findNode == root)
            return null;
        if (findNode.getIsWordComplete()){
            return findNode;
          }
        return null;
        }

        public String deleteACharacter(String word, int index){
          StringBuilder new_word = new StringBuilder(word);
          new_word.deleteCharAt(index);
          return new_word.toString();
        }

        public String insertCharacter(String word, int index){
        map_of_inserted_chars.clear();
           char c;
           int temp = 97;
            for(int i = 0; i < 26;i++){
                StringBuilder new_word = new StringBuilder(word);
                temp = temp + i;
               c = (char)temp;
               new_word.insert(index, c);
                arraysOfInsertedCharacters[insertedIndex] = new_word.toString();
                INode insertNode = find(new_word.toString());

                if(insertNode != null){
                    map_of_inserted_chars.put(new_word.toString(), insertNode.getValue());
                }
                temp = 97;
                insertedIndex++;
            }
            if(map_of_inserted_chars.isEmpty()){
                return "";
            }
            return findMax(map_of_inserted_chars);
        }


    public String alternateCharacter(String word, int index){
        char ch;
        int temp = 97;
        for(int i = 0; i < 26;i++){
            StringBuilder new_word = new StringBuilder(word);
            temp = temp + i;
            ch = (char)temp;
            new_word.setCharAt(index, ch);
            arrayOfAlternateCharacters[alternate_index] = new_word.toString();
            INode insertNode = find(new_word.toString());

            if(insertNode != null){
                map_of_inserted_chars.put(new_word.toString(), insertNode.getValue());
            }
            temp = 97;
            alternate_index++;
        }
        if(map_of_inserted_chars.isEmpty()){
            return "";
        }
        return findMax(map_of_inserted_chars);
    }

    public String tranposeCharacters(String word,int index){
        StringBuilder transpose_word = new StringBuilder(word);
        transpose_word.setCharAt(index, word.charAt(index + 1));
        transpose_word.setCharAt(index + 1, word.charAt(index));
        return transpose_word.toString();
    }


        public String findMax(Map<String, Integer> map){
        String key = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
         return key;
        }

    public String deletionDistance(String deletionWord){
        if(deletionWord.isEmpty()){
            return null;
        }
        Map<String,Integer> map_of_deleted_chars = new HashMap<>();
        StringBuilder temp = new StringBuilder(deletionWord);
        for(int i = 0; i < temp.length();i++){
            char a = temp.charAt(i);
            if(Character.isUpperCase(a)) {
                temp.setCharAt(i, Character.toLowerCase(a));
            }
        }

        deletionWord = temp.toString();

        arrayOfDeletedCharacters = new String[deletionWord.length()];
        int [] arraysOfCountFrequency;
        arraysOfCountFrequency = new int[deletionWord.length()];
        for(int i = 0; i < deletionWord.length();i++){
            arrayOfDeletedCharacters[i] = deleteACharacter(deletionWord, i);
        }
        for(int i = 0; i < arrayOfDeletedCharacters.length;i++){
            INode deletedNode = find(arrayOfDeletedCharacters[i]);

            if((deletedNode != null)){
                map_of_deleted_chars.put(arrayOfDeletedCharacters[i],deletedNode.getValue());
            }
        }

             if(map_of_deleted_chars.isEmpty()){
                 return null;
             }else{
                 return findMax(map_of_deleted_chars);
             }

    }

    public String insertionDistance(String edit_word){
        if(edit_word.isEmpty()){
            return null;
        }
        Map<String,Integer> insertion_distance = new HashMap<>();
        StringBuilder temps = new StringBuilder(edit_word);
        for(int i = 0; i < temps.length();i++){
            char a = temps.charAt(i);
            if(Character.isUpperCase(a)) {
                temps.setCharAt(i, Character.toLowerCase(a));
            }
        }

        edit_word = temps.toString();
        String [] temp;
        temp = new String[edit_word.length() + 1];
        arraysOfInsertedCharacters = new String[((edit_word.length() + 1) * 26)];
        for(int i = 0; i < edit_word.length() + 1;i++){
            temp[i] = insertCharacter(edit_word,i);
        }

        for(int i = 0; i < temp.length;i++){
            INode findNode = find(temp[i]);

            if(findNode != null){
                insertion_distance.put(temp[i],findNode.getValue());
            }
        }

        insertedIndex = 0;

        if(insertion_distance.isEmpty()){
            return null;
        }else{
            return findMax(insertion_distance);
        }
    }

    public String alterationDistance(String edit_word){
        if(edit_word.isEmpty()){
            return null;
        }
        Map<String,Integer> alteration_distance = new HashMap<>();
        StringBuilder temps = new StringBuilder(edit_word);
        for(int i = 0; i < temps.length();i++){
            char a = temps.charAt(i);
            if(Character.isUpperCase(a)) {
                temps.setCharAt(i, Character.toLowerCase(a));
            }
        }
        edit_word = temps.toString();
        String [] temp;
        temp = new String[edit_word.length()];
        arrayOfAlternateCharacters = new String[edit_word.length()* 26];
        for(int i = 0; i < edit_word.length();i++){
            temp[i] = alternateCharacter(edit_word,i);
        }

        for(int i = 0; i < temp.length;i++){
            INode alterationNode = find(temp[i]);

            if(alterationNode != null){
                alteration_distance.put(temp[i],alterationNode.getValue());
            }
        }

        alternate_index = 0;
        if(alteration_distance.isEmpty()){
            return null;
        }else{
            return findMax(alteration_distance);
        }
    }

    public String [] combineArrays(){
        int new_array_index = 0;
        String [] new_array = new String[arrayOfDeletedCharacters.length + arrayOfTransposeWords.length
        + arraysOfInsertedCharacters.length + arrayOfAlternateCharacters.length];

        for(int i = 0; i < arrayOfDeletedCharacters.length;i++){
            new_array[new_array_index] = arrayOfDeletedCharacters[i];
            new_array_index++;
        }
        for(int i = 0; i < arraysOfInsertedCharacters.length;i++){
            new_array[new_array_index] = arraysOfInsertedCharacters[i];
            new_array_index++;
        }
        for(int i = 0; i < arrayOfAlternateCharacters.length;i++){
            new_array[new_array_index] = arrayOfAlternateCharacters[i];
            new_array_index++;
        }
        for(int i = 0; i < arrayOfTransposeWords.length;i++){
            new_array[new_array_index] = arrayOfTransposeWords[i];
            new_array_index++;
        }
        return new_array;
    }

    public String second_edit(){
        String  []edit_arrays = combineArrays();
        Map<String,Integer> map_of_edit_words = new HashMap();
        for(int i = 0; i < edit_arrays.length;i++){
            Map<String,Integer> edit_words = new HashMap();
            if(deletionDistance(edit_arrays[i]) != null){
                INode deletedNode = find(deletionDistance(edit_arrays[i]));
                edit_words.put(deletionDistance(edit_arrays[i]), deletedNode.getValue());
            }

            if(insertionDistance(edit_arrays[i]) != null){
                INode insertedNode = find(insertionDistance(edit_arrays[i]));
                edit_words.put(insertionDistance(edit_arrays[i]), insertedNode.getValue());
            }

            if(alterationDistance(edit_arrays[i]) != null){
                INode alternateNode = find(alterationDistance(edit_arrays[i]));
                edit_words.put(alterationDistance(edit_arrays[i]), alternateNode.getValue());
            }

            if(tranpositionDistance(edit_arrays[i]) != null){
                INode transposeNode = find(tranpositionDistance(edit_arrays[i]));
                edit_words.put(tranpositionDistance(edit_arrays[i]), transposeNode.getValue());
            }
             if(edit_words.isEmpty()){

             }else {
                 String test = findMax(edit_words);
                 if (!test.isEmpty()) {
                     INode newNode = find(test);
                     map_of_edit_words.put(test, newNode.getValue());

                 }
             }

        }

        if(map_of_edit_words.isEmpty()){
            return null;
        }
        else{
            return findMax(map_of_edit_words);
        }

    }


    @Override
    public int hashCode() {
        int hash = 5;
        INode find = this.find(this.name);
        hash = 89 + hash + this.num_of_nodes*this.num_of_words*this.toString().hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }

        if(obj == this){
            return true;
        }

        if(obj.getClass() != this.getClass()){
            return false;
        }
        Trie newNode = (Trie)obj;

       if(this.name == null || newNode.name == null){
           return (newNode.num_of_nodes == this.num_of_nodes &&(newNode.num_of_words == this.num_of_words));
       }
        INode find = newNode.find(newNode.name);
        INode findtest = this.find(this.name);
        int x = find.getValue();
        int y = findtest.getValue();
        return (newNode.num_of_nodes == this.num_of_nodes && (newNode.name.equals(this.name)) &&
                (newNode.num_of_words == this.num_of_words) &&(find.getValue() == findtest.getValue()));
    }


    public String test__to_string(StringBuilder test, StringBuilder testString, Node curr_node){
           if(curr_node == null){
               return " ";
           }

           if(curr_node.getValue() > 0 &&(curr_node.getIsWordComplete())){
               test.append(testString.toString()).append("\n");
           }

           for(char i = 'a'; i <= 'z';i++){
               //Node newNode = curr_node.arrayList[i];
                 testString.append(i);
                 test__to_string(test, testString, curr_node.arrayList[i-'a']);
                 testString.deleteCharAt(testString.length()-1);
           }

           return test.toString();

    }
    @Override
    public String toString() {
        StringBuilder test_strings = new StringBuilder();
        StringBuilder test = new StringBuilder();
        return test__to_string(test_strings, test, root);
    }

    public String tranpositionDistance(String edit_word){

        if(edit_word.isEmpty()){
            return null;
        }

        Map<String,Integer> transpose_distance = new HashMap<>();
        StringBuilder temp = new StringBuilder(edit_word);
        for(int i = 0; i < temp.length();i++){
            char a = temp.charAt(i);
            if(Character.isUpperCase(a)) {
                temp.setCharAt(i, Character.toLowerCase(a));
            }
        }

        edit_word = temp.toString();
        arrayOfTransposeWords = new String[edit_word.length()-1];
        for(int i = 0; i < (edit_word.length() - 1);i++){
            arrayOfTransposeWords[i] = tranposeCharacters(edit_word, i);
        }

        for(int i = 0; i < arrayOfTransposeWords.length;i++){
            INode deletedNode = find(arrayOfTransposeWords[i]);

            if(deletedNode != null){
                transpose_distance.put(arrayOfTransposeWords[i],deletedNode.getValue());
            }
        }

        if(transpose_distance.isEmpty()){
            return null;
        }else{
            return findMax(transpose_distance);
        }
        //return "";


    }


    public void findCorrector(String word){
        Node findNode = new Node();
        findNode = (Node) find(word);
    }

    public int incrementNode() {
        return num_of_nodes++;
    }

    @Override
    public int getWordCount() {
        return num_of_words;
    }

    @Override
    public int getNodeCount() {
        return num_of_nodes;
    }

}
