
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

/**
 * Implement Shortest Ancestral Path (SAP).
 *
 * http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
 *
 * @author devin
 */
public class SAP {

    private final Digraph digraph;
    
    /**
     * Helper class to encapsulate ancestor vertex and distance (from two queried
     * vertices).
     */
    private static class AdjacentVertex {

        private final int vertex;
        private final int distance;

        public AdjacentVertex(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public int getVertex() {
            return vertex;
        }
        
        public int getDistance() {
            return distance;
        }

        @Override
        public String toString() {
            return "vertex=" + vertex + ", distance=" + distance;
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.IllegalArgumentException("input Digraph is null");
        }
        digraph = new Digraph(G);
    }

    // could rely on the digraph API
    private void checkVertexBounds(Integer vertex) {
        if (vertex == null || vertex < 0) {
            throw new java.lang.IllegalArgumentException("checkVertexBounds: invalid input vertex");
        }
    }

    private AdjacentVertex calculateWithPaths(BreadthFirstDirectedPaths vPath, BreadthFirstDirectedPaths wPath) {

        AdjacentVertex av = null;
            
        // where does v and w intersect?
        int minPathLength = Integer.MAX_VALUE;

        //check each vertex
        for (int i = 0; i < digraph.V(); i++) {

            //is this vertex reachable by both paths vPath and wPath?
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {

                //this vertex is a common ancestor, now check for the one with
                //the shortest path
                int distance = vPath.distTo(i) + wPath.distTo(i);

                if (distance < minPathLength) {
                    minPathLength = distance;
                    av = new AdjacentVertex(i, distance);
                }
            }
        }
        
        return av;
    }

    /**
     * Generate paths for vertices v, w. Used for length / ancestor method calls
     * with single vertex inputs. 
     * 
     * @param v
     * @param w
     * @return 
     */
    private AdjacentVertex calculate(int v, int w) {
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);
        return calculateWithPaths(vPath, wPath);
    }
    
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkVertexBounds(v);
        checkVertexBounds(w);

        if (v == w) {
            return 0;
        }

        AdjacentVertex av = calculate(v, w);
        if (av != null) {
            return av.getDistance();
        } else {
            return -1;
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkVertexBounds(v);
        checkVertexBounds(w);

        if (v == w) {
            return v;
        }

        AdjacentVertex av = calculate(v, w);
        if (av != null) {
            return av.getVertex();
        } else {
            return -1;
        }
    }

    private AdjacentVertex calculate(Iterable<Integer> v, Iterable<Integer> w) {
        
        if(v == null || w == null) {
            throw new IllegalArgumentException();
        }
        
        //have to check all combinations of v and w
        for (Integer i : v) {
            checkVertexBounds(i);
        }
        for (Integer i : w) {
            checkVertexBounds(i);
        }

        AdjacentVertex bestVertex = null;

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(digraph, w);

        AdjacentVertex av = calculateWithPaths(vPath, wPath);
        if (av != null) {
            if (bestVertex == null) {
                bestVertex = av;
            } else if (av.getDistance() < bestVertex.getDistance()) {
                bestVertex = av;
            }
        }
        return bestVertex;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        AdjacentVertex av = calculate(v, w);
        if (av != null) {
            return av.getDistance();
        } else {
            return -1;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        AdjacentVertex av = calculate(v, w);
        if (av != null) {
            return av.getVertex();
        } else {
            return -1;
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
