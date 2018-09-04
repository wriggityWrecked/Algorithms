import edu.princeton.cs.algs4.In;
import java.util.HashMap;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdIn;
import java.util.ArrayList;

/**
 *
 * http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
 *
 * @author devin
 */
public class WordNet {

    private final RedBlackBST<String, ArrayList<Integer>> sysnetTree;
    private final HashMap<Integer, String> sysnetHash;
    private final Digraph graph;
    
    private Digraph getDigraph() {
        return graph;
    }
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        
        if(synsets == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if(hypernyms == null) {
            throw new java.lang.IllegalArgumentException();
        }
        
        sysnetHash = new HashMap<>();
        sysnetTree = new RedBlackBST<>();
        
        int numberOfSysnets = parseSynset(synsets);
        
        graph = new Digraph(numberOfSysnets);
        parseHypernyms(hypernyms);
        
        // check if rooted
        int rootCount = 0;
        for(int i=0; i<graph.V(); i++) {
            if(graph.outdegree(i) == 0) {
                rootCount++;
            }
        }
        if(rootCount != 1) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    private int parseSynset(String fileName) {
        int count = 0;
        In in = new In(fileName);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] split = line.split(",");
            
            int id = Integer.parseInt(split[0]);
            sysnetHash.put(id, split[1]);
            
            String[] nouns = split[1].split(" ");
            for(String noun: nouns) {
                ArrayList<Integer> ids; 
                if(!sysnetTree.contains(noun)) {
                    ids = new ArrayList<>();
                    sysnetTree.put(noun, ids);
                } else {
                    ids = sysnetTree.get(noun);
                }
                ids.add(id);
            }
            count++;
        }
        return count;
    }
    
    private void parseHypernyms(String fileName) {

        In in = new In(fileName);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] split = line.split(",");
            
            int v = Integer.parseInt(split[0]);
            for(int i=1; i<split.length; i++) {
                int w = Integer.parseInt(split[i]);
                graph.addEdge(v, w);
            }
        }
    }

    public Iterable<String> nouns() {
        return sysnetTree.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if(word == null) {
            throw new java.lang.IllegalArgumentException();
        }

        return sysnetTree.contains(word);
    }

    
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if(nounA == null || !isNoun(nounA)) {
            throw new java.lang.IllegalArgumentException();
        }
        if(nounB == null || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }
        
        SAP sap = new SAP(graph);
        ArrayList<Integer> vs = sysnetTree.get(nounA);
        ArrayList<Integer> ws = sysnetTree.get(nounB);
        
        if(vs.size() == 1 && ws.size() == 1) {
            return sap.length(vs.get(0), ws.get(0));
        } else {
            return sap.length(vs, ws);
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if(nounA == null || !isNoun(nounA)) {
            throw new java.lang.IllegalArgumentException();
        }
        if(nounB == null || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }
        
        SAP sap = new SAP(graph);
        ArrayList<Integer> vs = sysnetTree.get(nounA);
        ArrayList<Integer> ws = sysnetTree.get(nounB);
        
        int id;
        if(vs.size() == 1 && ws.size() == 1) {
            id = sap.ancestor(vs.get(0), ws.get(0));
        } else {
            id = sap.ancestor(vs, ws);
        }
        
        //what happens if ID == -1?
        if(id != -1) {
            return sysnetHash.get(id);
        } else {
            return "-1";
        }
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        String filename1 = args[0];
        String filename2 = args[1];
        WordNet wn = new WordNet(filename1, filename2);

        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int length = wn.distance(v, w);
            String ancestor = wn.sap(v, w);
            System.out.println("length = " + length +", ancestor = " + ancestor + "\n");
        }
    }
}
