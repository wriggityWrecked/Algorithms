import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 *
 * http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
 *
 * @author devin
 */

public class Outcast {

    private final WordNet wordnet;
    
    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        this.wordnet = wordnet; //TODO defensive copy constructor?
    }

    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        
        //do all pairwise comparisons
        
        int index = -1;
        int maxDistance = Integer.MIN_VALUE;
        
        for(int i=0; i<nouns.length; i++) {
            int iDistance = 0;
            for(int j=0; j<nouns.length; j++) {
                if(i == j) {
                    continue;
                } else {
                    iDistance += wordnet.distance(nouns[i], nouns[j]);
                }
            }
            if(iDistance > maxDistance) {
                maxDistance = iDistance;
                index = i;
            }
        }
        
        return nouns[index];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}
