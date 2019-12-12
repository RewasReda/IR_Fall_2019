package guiBooleanModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class pTerm {
    public int documentFrequency = 0;
    public ArrayList<Integer> documentNumber;
    public ArrayList<ArrayList<Integer>> documentPosition;
//    public Map<Integer, Integer> tfHashMap = new HashMap<Integer, Integer>();

    
    public pTerm ( ) {
        documentNumber = new ArrayList<Integer>();
        documentPosition = new ArrayList<ArrayList<Integer>>();
    }
}
