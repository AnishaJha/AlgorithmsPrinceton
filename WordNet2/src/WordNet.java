import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WordNet {
    Digraph dag;
    HashMap<String, ArrayList<Integer>> hm;
    String[] synsets;
    SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        if (synsets == null)
            throw new IllegalArgumentException("synsets is null");
        if(hypernyms == null)
            throw new IllegalArgumentException("hypernyms is null");
        hm = new HashMap<>();
        In in = new In(synsets);
        String[] linesSyn = in.readAllLines();
        in = new In(hypernyms);
        String[] linesHyper = in.readAllLines();
        //String hyper = null;
        String[] splitSynset, hypernymlist;
        int len = linesSyn.length;
        //nouns = new String[len];
        //String[] synSetList = new String[len];
        this.synsets = new String[len];
        //int count = 0;
        dag = new Digraph(len);
        try {
            for (int i = 0; i < len; i++) {
                splitSynset = linesSyn[i].split(",");
                if (splitSynset.length != 3)
                    throw new IllegalArgumentException("Input format of synsets not correct");
                this.synsets[i] = splitSynset[1];
                String[] nounList = splitSynset[1].split(" ");
                for (String noun : nounList) {
                    if (!hm.containsKey(noun)) {
                        ArrayList<Integer> al = new ArrayList<>();
                        al.add(i);
                        hm.put(noun, al);
                    }
                    else {
                        ArrayList<Integer> al = hm.get(noun);
                        al.add(i);
                        hm.put(noun, al);
                    }
                }
                hypernymlist = linesHyper[i].split(",");
                for (String hypernym : hypernymlist) {
                    int vertex = Integer.parseInt(hypernym);
                    if (vertex != i)
                        dag.addEdge(i, vertex);
                }
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Input does not correspomd to rooted DAG");
        }
        sap = new SAP(dag);

    }
    /*
    private Integer[] combine(Integer[] arr1, int num) {
        Integer[] arr2;
        if (arr1 == null) {
            return new Integer[]{num};
        }
        else {
            int len = arr1.length;
            arr2 = new Integer[len + 1];
            System.arraycopy(arr1, 0, arr2, 0, len);
            arr2[len] = num;
        }
        return arr2;
    }

     */

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return hm.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        if (word == null)
            throw new IllegalArgumentException("Input word for isNoun is null");
        return hm.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        if (nounA == null)
            throw new IllegalArgumentException("nounA for distance is null");
        if (nounB == null)
            throw new IllegalArgumentException("nounB for distance is null");
        ArrayList<Integer> hypernymsA = hm.get(nounA);
        ArrayList<Integer> hypernymsB = hm.get(nounB);
        if (hypernymsA == null || hypernymsB == null)
            throw new IllegalArgumentException("Noun for distance not found in the input");
        return sap.length(hypernymsA, hypernymsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        if (nounA == null)
            throw new IllegalArgumentException("nounA for sap is null");
        if (nounB == null)
            throw new IllegalArgumentException("nounB for sap is null");
        ArrayList<Integer> hypernymsA = hm.get(nounA);
        ArrayList<Integer> hypernymsB = hm.get(nounB);
        if (hypernymsA == null || hypernymsB == null)
            throw new IllegalArgumentException("Noun for sap not found in the input");
        int ancestor = sap.ancestor(hypernymsA, hypernymsB);
        if (ancestor == -1 || ancestor >= synsets.length)
            return null;
        return synsets[ancestor];
    }

    // do unit testing of this class
    public static void main(String[] args){

    }
}

