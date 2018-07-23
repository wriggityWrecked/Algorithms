
import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {

        // read k
        int k = Integer.parseInt(args[0]);
        
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String read = StdIn.readString();
            rq.enqueue(read);
        }
        
        while(k-- > 0) {
            System.out.println(rq.dequeue());
        }
    }
}
