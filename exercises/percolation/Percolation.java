
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 * @author devin
 */
public class Percolation {

    private final WeightedQuickUnionUF unionFind;
    private final boolean[] isOpen;
    // keep track of the components connected to bottom, not sites
    private final boolean[] connectedToBottom;
    // don't need to do the same for the top if we keep a virtual top node
    // this saves memeory for an array of size n, while only adding a single
    // site for the WeightedQuickUnionUF. 
    private int numberOfOpenSites = 0;
    private final int n;
    private boolean percolates = false;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("Invalid input " + n
                    + ", must be > 0");
        }

        this.n = n; // total size
        isOpen = new boolean[n * n]; // all false, 0 to n-1
        connectedToBottom = new boolean[n * n]; // all false, 0 to n-1
        unionFind = new WeightedQuickUnionUF(n * n + 1);

        for (int i = (n * n - n); i < connectedToBottom.length; i++) {
            connectedToBottom[i] = true;
        }
    }

    /**
     * Return the transform of a row, column (1,1 to n,n) to the 1-d array
     * mapping from 0 to n-1.
     *
     * @param row
     * @param col
     * @return
     */
    private int rowAndColumnToIndex(int row, int col) {

        if (row <= 0 || row > n) {
            throw new IllegalArgumentException("Input row: " + row
                    + " out of range");
        }
        if (col <= 0 || col > n) {
            throw new IllegalArgumentException("Input col:"
                    + col + " out of range");
        }

        return ((row - 1) * n) + (col - 1);
    }

    /**
     * Handle the union between two open sites. Assumes both i and j are open. 
     * 
     * @param i
     * @param j 
     */
    private void handleUnion(int i, int j) {

        int root1 = unionFind.find(i);
        int root2 = unionFind.find(j);

        unionFind.union(i, j);
        // check if root component is connected to top?
        // check if root component is connected to bottom?

        if (connectedToBottom[root1]) {
            connectedToBottom[root2] = true;
        }
        if (connectedToBottom[root2]) {
            connectedToBottom[root1] = true;
        }
        
        checkIfPercolates(j);
    }

    // open site (row, col) if it is not open already 
    public void open(int row, int col) {

        int i = rowAndColumnToIndex(row, col);

        // is this already opened? if so do nothing
        if (isOpen[i]) {
            return;
        }

        // connect to virtual top if needed
        if (row == 1) {
            unionFind.union(i, n*n);
        }
        
        // mark as opened
        isOpen[i] = true;

        // cache open count
        numberOfOpenSites++;

        // go through each site: below, above, left, right
        try {
            int j = rowAndColumnToIndex(row - 1, col);
            if (isOpen[j]) {
                handleUnion(i, j);
            }
        } catch (IllegalArgumentException e) {
            // do nothing
        }

        try {
            int j = rowAndColumnToIndex(row + 1, col);
            if (isOpen[j]) {
                handleUnion(i, j);
            }
        } catch (IllegalArgumentException e) {
            // do nothing
        }

        try {
            int j = rowAndColumnToIndex(row, col - 1);
            if (isOpen[j]) {
                handleUnion(i, j);
            }
        } catch (IllegalArgumentException e) {
            // do nothing
        }

        try {
            int j = rowAndColumnToIndex(row, col + 1);
            if (isOpen[j]) {
                handleUnion(i, j);
            }
        } catch (IllegalArgumentException e) {
            // do nothing
        }

        checkIfPercolates(i);
    }

    private void checkIfPercolates(int index) {

        if (percolates) {
            return;
        }
        int component = unionFind.find(index);

        // connected to both top and bottom and open?
	// bottom is by component, top is by virtual node
        if (isOpen[index] && connectedToBottom[component] && unionFind.connected(index, n*n)) {
            percolates = true;
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        return isOpen[rowAndColumnToIndex(row, col)];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {

        int i = rowAndColumnToIndex(row, col);

        // no point in checking if connected if blocked / not open
        if (!isOpen[i]) {
            return false;
        }

        // open and connected to virtual top?
        return unionFind.connected(i, n*n);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?   
    public boolean percolates() {
        return percolates;
    }

    public static void main(String[] args) {

        Percolation pp = new Percolation(5);

        // test initialization
        assert (!pp.percolates());
        assert (0 == pp.numberOfOpenSites());

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                assert (!pp.isOpen(i, j));
                assert (!pp.isFull(i, j));
            }
        }
    }  // test client (optional)
}
