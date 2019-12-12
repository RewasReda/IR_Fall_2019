package guiBooleanModel;

import java.util.ArrayList;
import java.util.Arrays;
import org.tartarus.snowball.ext.PorterStemmer;


public class Tokenizer {
    public String tokenize ( String str ) {
        str = str.toLowerCase();
        
        String toReturn = "";
        String[] array = str.split(" ");
        PorterStemmer stemmer = new PorterStemmer();
        for ( int i=0; i<array.length; ++i ) {
            array[i].toLowerCase();
            stemmer.setCurrent(array[i]);
            stemmer.stem();
            array[i] = stemmer.getCurrent();
            toReturn = toReturn + removeSpecial(array[i]) + ' ';
        
        }
            
        return toReturn;
    }
    
    public String removeStopword ( String str, ArrayList<String> stopword ) {
        String toReturn = "";
        String[] array = str.split(" ");
        
        for ( int i=0; i<array.length; ++i ) {
            if ( !(stopword.contains(array[i])) ) toReturn = toReturn + array[i] + ' ';
        }
        
        return toReturn;
    }
    
    private String removeSpecial ( String str ) {
        return str.replaceAll("[^a-zA-Z0-9]", "");
    }
    

}
