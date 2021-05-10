package spell;
import java.util.HashMap;
import java.util.Map;

public class Node  implements INode{

    //Map<Character, Node> children = new HashMap<>();
    final int MAX = 24;
    Node [] arrayList;
    boolean is_word_complete;
    int num_of_words;
    char characterValue;

    int word_count;

    public Node(){
        this.word_count = 0;
        this.num_of_words = 1;
        this.is_word_complete = false;
        arrayList = new Node[26];
    }

    public void setValue(int value){
        word_count = value;
    }
    @Override
    public int getValue() {
        return num_of_words;
    }

    boolean isCharacterValid(char testCharacter){
        return true;
    }

    void setWordComplete(boolean is_word_complete) {
        this.is_word_complete = is_word_complete;
    }

    boolean getIsWordComplete(){
        return is_word_complete;
    }
}
