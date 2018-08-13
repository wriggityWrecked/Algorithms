
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 *
 * @author devin
 */
public class Board {

    private final int[][] board; //TODO 1-D array
    private final int[] board2; //TODO 1-D array

    private final int dimension;
    private int hamming = -1;
    private int manhattan = -1;
    private Board twin = null;
    
    public Board(int[][] blocks) {

        if(blocks == null) {
            throw new IllegalArgumentException("input board cannot be null");
        }
        
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        dimension = blocks[0].length;
        board = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(blocks[i], 0, board[i], 0, dimension);
        }
        
        board2 = new int[dimension * dimension];
        int index = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board2[index++] = blocks[i][j];
            }
        }
    }

    private int getBlock(int i, int j) {
        return board[i][j];
    }
    
    public int dimension() {
        // board dimension n
        return dimension;
    }

    public int hamming() {
        // number of blocks out of place
        if (hamming == -1) {
            hamming = 0;
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    
                    int blockValue = getBlock(i,j);
                    
                    if (blockValue == 0) {
                        //skip the empty space
                        continue;
                    }
                    if (getCorrectBlockFromPosition(i, j) != blockValue) {
                        hamming++;
                    }
                }
            }
        }

        return hamming;
    }

    private int getCorrectBlockFromPosition(int i, int j) {
        if (i == dimension() - 1 && j == dimension() - 1) {
            //last block, expect 0
            return 0;
        }
        return dimension() * i + j + 1;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        if (manhattan == -1) {
            
            manhattan = 0;
            
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    
                    int blockValue = getBlock(i,j);
                    
                    //skip the empty space
                    if (blockValue == 0) {
                        continue;
                    }
                        
                    //get the coordinates for this value
                    int desired_i = (blockValue - 1) / dimension(); //height
                    int desired_j = (blockValue - 1) % dimension(); //width

                    //get the distance / difference
                    manhattan += Math.abs(desired_i - i) + Math.abs(desired_j - j);
                }
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        // is this board the goal board?
        
        //easy check, if the last square is not the goal then exit
        if(getBlock(dimension - 1, dimension - 1) != 0) {
            return false;
        } 
        
        for (int i = 0; i < dimension(); i++) {
            
            for (int j = 0; j < dimension(); j++) {
                
                int value = getBlock(i,j);
                
                int expectedValue = getCorrectBlockFromPosition(i, j);
                if (value != expectedValue) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board twin() {
        
        if(twin == null) {
            int i1;
            int j1;

            do {

                i1 = StdRandom.uniform(dimension());
                j1 = StdRandom.uniform(dimension());

             } while(getBlock(i1,j1) == 0);

            //exit when value is 0

            int i2;
            int j2;

            do {

                i2 = StdRandom.uniform(dimension());
                j2 = StdRandom.uniform(dimension());

            } while(getBlock(i2,j2) == 0 || getBlock(i1,j2) == getBlock(i2,j2));

            //swap indices
            int tmp = board[i2][j2];
            board[i2][j2] = board[i1][j1];
            board[i1][j1] = tmp;

            //copies array
            twin = new Board(board);

            //swap back
            board[i1][j1] = board[i2][j2];
            board[i2][j2] = tmp;
        }
        return twin;
    }
    
    @Override
    public boolean equals(Object y) {
        
        if (y == null || !(y instanceof Board)) {
            return false;
        }
        
        if(y == this) {
            return true;
        }

        Board toCheck = (Board) y;

        if (toCheck.dimension() != dimension()) {
            return false;
        }
        // distances may not be cached, so brute force is quicker unless equal (or almost)
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (toCheck.board[i][j] != board[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        
        ArrayList<Board> neighbors = new ArrayList<>();
        // all neighboring boards
        //first find 0

        for (int i = 0; i < dimension; i++) {

            for (int j = 0; j < dimension; j++) {

                if (board[i][j] == 0) {

                    //4 possible neighbors
                    int candidate_i = i + 1;
                    int candidate_j = j;
                    
                    //todo messy the below could be a full function
                    
                    if (candidate_i >= 0 && candidate_i < dimension()
                            && candidate_j >= 0 && candidate_j < dimension()) {
                        // coordinates are acceptable, now need to swap and generate board
                        swapWithZero(board, i, j, candidate_i, candidate_j);
                        Board b = new Board(board);
                        neighbors.add(b);
                        //get original
                        swapWithZero(board, candidate_i, candidate_j, i, j);
                    }
                    //=====================================================
                    candidate_i = i - 1;
                    candidate_j = j;

                    if (candidate_i >= 0 && candidate_i < dimension()
                            && candidate_j >= 0 && candidate_j < dimension()) {
                        // coordinates are acceptable, now need to swap and generate board
                        swapWithZero(board, i, j, candidate_i, candidate_j);
                        Board b = new Board(board);
                        neighbors.add(b);
                        //get original
                        swapWithZero(board, candidate_i, candidate_j, i, j);
                    }

                    //=====================================================
                    candidate_i = i;
                    candidate_j = j + 1;

                    if (candidate_i >= 0 && candidate_i < dimension()
                            && candidate_j >= 0 && candidate_j < dimension()) {
                        // coordinates are acceptable, now need to swap and generate board
                        swapWithZero(board, i, j, candidate_i, candidate_j);
                        Board b = new Board(board);
                        neighbors.add(b);
                        //get original
                        swapWithZero(board, candidate_i, candidate_j, i, j);
                    }
                    //=====================================================
                    candidate_i = i;
                    candidate_j = j - 1;

                    if (candidate_i >= 0 && candidate_i < dimension()
                            && candidate_j >= 0 && candidate_j < dimension()) {
                        // coordinates are acceptable, now need to swap and generate board
                        swapWithZero(board, i, j, candidate_i, candidate_j);
                        Board b = new Board(board);
                        neighbors.add(b);
                        //get original
                        swapWithZero(board, candidate_i, candidate_j, i, j);
                    }
                    
                    return neighbors;
                }
            }
        }
        return neighbors;
    }

    private void swapWithZero(int[][] board, int i, int j, int new_i, int new_j) {
        //0 is at i,j
        board[i][j] = board[new_i][new_j];
        board[new_i][new_j] = 0;
    }

    @Override
    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder sb = new StringBuilder();
        sb.append(dimension()).append("\n");
        for (int i = 0; i < dimension(); i++) {
            sb.append(" ");
            for (int j = 0; j < dimension(); j++) {
                sb.append(" ").append(String.format("%2d", board[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public static void main(String[] args) {

        int[][] blocks = new int[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 3) {
                    blocks[i][j] = 0;
                } else {
                    blocks[i][j] = 4 * i + j + 1;
                }
            }
        }

        Board initial = new Board(blocks);
        System.out.println(initial);
        assert (initial.hamming() == 0);
        assert (initial.manhattan() == 0);
        assert (initial.isGoal());
        assert (initial.equals(initial));

        // unit tests (not graded)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board second = new Board(blocks);
        assert (!second.isGoal());
        assert (!second.equals(initial));
        System.out.println(second);

        Iterator<Board> neighbors = second.neighbors().iterator();
        while (neighbors.hasNext()) {
            System.out.println(neighbors.next());
        }
        System.out.println("========");
        for(int i=0; i<5; i++) {
            System.out.println(initial.twin());
        }
        
    }
}
