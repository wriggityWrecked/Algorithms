
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import java.util.LinkedList;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 *
 * @author devin
 */
public class Solver {

    private final int numberOfMoves;
    private final boolean isSolveable;
    private final LinkedList<Board> sequenceToSolution;

    private class SearchNode implements Comparable<SearchNode> {

        private final SearchNode parent;
        private final Board node;
        private final int numberOfMoves;

        public SearchNode(Board board, SearchNode parent, int numberOfMoves) {
            this.node = board;
            this.parent = parent;
            this.numberOfMoves = numberOfMoves;
        }

        public Board getBoard() {
            return node;
        }

        public SearchNode getParent() {
            return parent;
        }

        public int priorityManhattan() {
            return node.manhattan() + numberOfMoves;
        }

        public int priorityHamming() {
            return node.hamming() + numberOfMoves;
        }

        @Override
        public int compareTo(SearchNode o) {
            if (o == null) {
                return -1;
            }

            int ret = Integer.compare(priorityManhattan(), o.priorityManhattan());
            if (ret == 0) {
                Integer.compare(priorityHamming(), o.priorityHamming());
            }
            if (ret == 0) {
                ret = Integer.compare(node.manhattan(), o.getBoard().manhattan());
            }
            if(ret == 0) {
                ret = Integer.compare(node.hamming(), o.getBoard().hamming());
            }
            return ret;
        }

        public int moves() {
            return numberOfMoves;
        }
    }

    public Solver(Board initial) {
        
        if(initial == null) {
            throw new IllegalArgumentException("input board cannot be null");
        }
        
        //is the initial board the solution?
        if (initial.isGoal()) {
            //done
            numberOfMoves = 0;
            isSolveable = true;
            sequenceToSolution = new LinkedList<>();
            sequenceToSolution.add(initial);
            return;
        }

        Board twin = initial.twin();

        // find a solution to the initial board (using the A* algorithm)
        boolean foundSolution = false;

        SearchNode node = new SearchNode(initial, null, 0);
        //twin
        SearchNode twinNode = new SearchNode(twin, null, 0);

        MinPQ<SearchNode> minPq = new MinPQ<>();
        //twin
        MinPQ<SearchNode> twinMinPq = new MinPQ<>();

        minPq.insert(node);
        //twin
        twinMinPq.insert(twinNode);
        
        while (!foundSolution) {
            
            //System.out.println("=======");
            boolean foundNeighbor = false;

            Board b = node.getBoard();
            SearchNode p = node.getParent();

            //remove min
            minPq.delMin();
            
            for (Board neighbor : b.neighbors()) {

                if (p != null && !foundNeighbor && p.getBoard().equals(neighbor)) {
                    //found the neighbor once, don't need to keep checking
                    foundNeighbor = true;
                    continue; //skip neighbor
                }

                SearchNode child = new SearchNode(neighbor, node, node.moves()+ 1);
                minPq.insert(child);
            }

            //get new min
            node = minPq.min();

            if (node.getBoard().isGoal()) {
                foundSolution = true;
            }

            //twin
            b = twinNode.getBoard();
            p = twinNode.getParent();
            foundNeighbor = false;

            for (Board neighbor : b.neighbors()) {

                if (!foundNeighbor && p != null && !p.getBoard().equals(neighbor)) {
                    foundNeighbor = true;
                    continue; //skip
                }
                SearchNode child = new SearchNode(neighbor, twinNode, twinNode.moves()+ 1);
                twinMinPq.insert(child);
            }

            //remove min
            twinMinPq.delMin();

            //get new min
            twinNode = twinMinPq.min();

            if (twinNode.getBoard().isGoal()) {
                //unsolveable
                numberOfMoves = -1;
                isSolveable = false;
                sequenceToSolution = null;
                return;
            }
        }

        numberOfMoves = node.moves();
        isSolveable = true;
        sequenceToSolution = new LinkedList<>();

        while (node != null) {
            sequenceToSolution.addFirst(node.getBoard());
            node = node.getParent();
        }
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return isSolveable;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        return numberOfMoves;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        return sequenceToSolution;
    }

    public static void main(String[] args) {
        // solve a slider puzzle (given below)

//        int n = 3;
//        int[][] blocks = new int[n][n];
//
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                if (i == n-1 && j == n-1) {
//                    blocks[i][j] = 0;
//                } else {
//                    blocks[i][j] = n * i + j + 1;
//                }
//            }
//        }
//
//        Board perfect = new Board(blocks);
//        Solver s = new Solver(perfect);
//        
//        assert(s.isSolvable());
//        assert(s.moves() == 0);
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            System.out.println("No solution possible");
        } else {
            System.out.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                System.out.println(board);
            }
        }
    }
}
