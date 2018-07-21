
import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {

        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        // read k
        while (!StdIn.isEmpty()) {
            // fill up the array until we hit size k, then start pulling off samples
            String read = StdIn.readString();
            rq.enqueue(read);
        }
        
        while(k-- > 0) {
            System.out.println(rq.dequeue());
        }
    }
}
