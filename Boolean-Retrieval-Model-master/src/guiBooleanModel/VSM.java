/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guiBooleanModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rewas
 */
public class VSM {
    public int DocNo;
    ArrayList<Double> docScores;
    public Map<String, ArrayList<Double>> TfIdfs;
    public Map<String, Double> IDFHashMap = new HashMap<String, Double>();
    public Map<String, HashMap<Integer, Double>> logtfHashMap = new HashMap<String, HashMap<Integer, Double>>();


    public VSM ( ) {
        TfIdfs = new HashMap<String, ArrayList<Double>>();
        IDFHashMap = new HashMap<String, Double>();
        docScores= new ArrayList<Double>();
        DocNo = 0;
    }
    
     public void getIDF (PositionalIndex PI) {
        this.DocNo = PI.DocNo;
        for ( Map.Entry<String, pTerm> entry : PI.dictionary.entrySet() ) {
            double idf = Math.log10(DocNo/entry.getValue().documentFrequency);
            this.IDFHashMap.put(entry.getKey(), idf);
        }
        for (Map.Entry<String, Double> entry : this.IDFHashMap.entrySet()) {
            System.out.println(entry.getKey()+ "IDF " + entry.getValue());

        }
    }
     public void getTFIDF (PositionalIndex PI) {
        getIDF(PI);
//        Map<String, HashMap<Integer, Integer>> TFHashMap = PI.tfHashMap;
        HashMap<Integer, Integer> freqMap;
        HashMap<Integer, Double> logfreqMap;
        double TF = 0;
        for (Map.Entry <String, HashMap<Integer, Integer>> entry : PI.tfHashMap.entrySet()) {
            freqMap = entry.getValue();
            logfreqMap = new HashMap<Integer,Double>();
            for (int i = 0 ; i<this.DocNo ; i++) {
                TF = freqMap.get(i+1);
                if(TF==0)
                {
                    logfreqMap.put(i+1, 0.0);
                }
                else
                {
                    TF=1+Math.log10(TF);
                    logfreqMap.put(i+1, TF);
                }
            
            }
            logtfHashMap.put(entry.getKey(), logfreqMap);

        }
                for (Map.Entry <String, HashMap<Integer, Double>> entry : logtfHashMap.entrySet()) {
                    System.out.println(entry.getKey() + "  TF  " +entry.getValue());
                }
        HashMap<Integer, Double> tf;
        ArrayList<Double> weights = new ArrayList<Double>();
        for (Map.Entry<String, Double> entry : this.IDFHashMap.entrySet()) {
            tf = logtfHashMap.get(entry.getKey());
            weights = new ArrayList<Double>();
            for (int i =0 ; i<tf.size() ; i++) {
                weights.add(i, tf.get(i+1) * entry.getValue() );
            }
            this.TfIdfs.put(entry.getKey(), weights);
        }
        
        for (Map.Entry <String, ArrayList<Double>> entry : this.TfIdfs.entrySet()) {
            System.out.println(entry.getKey() + "  TF-IDF  " +entry.getValue());

        }        
    }
     
         private ArrayList<Double> EuclideanDistance(PositionalIndex PI) {
        getTFIDF(PI);
        double Distance;
        ArrayList<Double> NormVec = new ArrayList<Double>();
        ArrayList<Double> tfidf;
        for(int i = 0 ; i <this.DocNo ; i++){
            Distance = 0;
            tfidf = new ArrayList<Double>();
            for (Map.Entry <String, ArrayList<Double>> entry : this.TfIdfs.entrySet()) {
                tfidf = entry.getValue();
                if(!tfidf.isEmpty()){
                Distance += Math.pow(tfidf.get(i), 2);
                }
            }
            NormVec.add(i, Math.sqrt(Distance));
        }
        return NormVec;
    }
         
           public void cosineSimilarity( String query, QueryHandler q ,PositionalIndex PI) {
            String[] array = query.split(" ");
            ArrayList<Double> DocQuerySim = new ArrayList<Double>();
            ArrayList<Double> NormVec = EuclideanDistance(PI);
            double queryLength = q.processFreeTextQueries(array, this.IDFHashMap);
            System.out.println("distances" + NormVec);
            if(queryLength != 0){
            double dot, norm = 0,dotsum;
            for(int i = 0 ; i <this.DocNo ; i++){
                dotsum = 0;
                dot = 0;
                for (String word : array) {
                    if(this.TfIdfs.containsKey(word)){
                        dot = this.TfIdfs.get(word).get(i) * this.IDFHashMap.get(word);
                        dotsum += dot;
                    }
            }
                norm = NormVec.get(i) * queryLength;
                DocQuerySim.add(dotsum/norm);

            }
            this.docScores = DocQuerySim;
        }
            else
            {
                System.out.println("Query is Empty");
            }
            System.out.println("scores" + DocQuerySim);
    }
     
           
    public ArrayList<Integer> rankedDoc( String query, QueryHandler q ,PositionalIndex PI) {
            cosineSimilarity(query,q,PI);
            ArrayList<Integer> Ranks = new ArrayList<Integer>();
            if(!this.docScores.isEmpty()){
            ArrayList<Double> Scores = this.docScores;
            Collections.sort(Scores);
            for(Double score : Scores){
                Ranks.add(this.docScores.indexOf(score));
            }
        }
        return Ranks;
    }  

}
