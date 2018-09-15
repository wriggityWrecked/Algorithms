import edu.princeton.cs.algs4.Picture;
import java.util.Arrays;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/seam.html
 *
 * @author devin
 */
public class SeamCarver {

    private Picture picture;
    private boolean transposed = false;
    private static final int BORDER_ENERGY = 1000;
    private static final int ENERGY_INIT_VALUE = -1;

    public SeamCarver(Picture picture) {
        // create a seam carver object based on the given picture

        if (picture == null) {
            throw new java.lang.IllegalArgumentException("input picture cannot be null");
        }
        // defensive copy, as we can't mutate the input
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        // current picture
        
        // transpose back if needed
        if (transposed) {
            transposePicture();
        }
        
        return new Picture(picture);
    }

    public int width() {
        // width of current picture
        
        if (transposed) {
            return picture.height();
        }

        return picture.width(); //columns
    }

    public int height() {
        // height of current picture

        if (transposed) {
            return picture.width();
        }
        
        return picture.height(); //rows
    }

    /**
     * Helper method to check if indices are valid.
     *
     * @param x
     * @param y
     */
    private void checkIndices(int row, int col) {
        if (row < 0 || col < 0 || row >= picture.width() || col >= picture.height()) {
            throw new java.lang.IllegalArgumentException("checkIndices invalid input:"
                    + " row=" + row + ", y=" + col);
        }
    }

    /**
     * Return true of the indices are valid, false otherwise.
     * 
     * @param row
     * @param col
     * @return 
     */
    private boolean checkIfIndicesValid(int row, int col) {
        return !(row < 0 || col < 0 || row >= picture.width() || col >= picture.height());
    }

    /**
     * Helper method to compute the energy of a pixel if not already done so.
     * Assumes that the input energy array is initialized / filled with
     * ENERGY_INIT_VALUE.
     *
     * @param row
     * @param col
     * @param energy
     */
    private void computeEnergy(int row, int col, double[][] energy) {
        
        // only compute if not done already
        if (energy[row][col] == ENERGY_INIT_VALUE) {
            energy[row][col] = energyNoTranspose(row, col);
        }
    }

    /**
     * Compute the energy of an input pixel (only if valid, does input checking
     * on the input indices).
     *
     * @param row
     * @param col
     * @return computed energy or BORDER_ENERGY (if pixel is on border)
     */
    public double energy(int row, int col) {
        
        if(transposed) {
            return energyNoTranspose(col, row);
        } else {
            return energyNoTranspose(row, col);
        }
    }
    
    /**
     * Helper used without the transpose flag. 
     * 
     * @param row
     * @param col
     * @return 
     */
    private double energyNoTranspose(int row, int col) {
        
        // energy of pixel at column x and row y
        checkIndices(row, col);

        // check if on border
        if (row == 0 || row == picture.width() - 1 || col == 0 
                || col == picture.height() - 1) {
            return BORDER_ENERGY;
        } else {
            double x_e = computeEnergy(row + 1, col    , row - 1, col);
            double y_e = computeEnergy(row    , col + 1, row    , col - 1);

            return Math.sqrt(x_e + y_e);
        } 
    }

    /**
     * Compute and return the energy of a non-border pixel. Helper method that
     * only computes a single direction (delta X or delta Y).
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private double computeEnergy(int x1, int y1, int x2, int y2) {

        int rgb     = picture.getRGB(x1, y1);
        int red_1   = (rgb >> 16) & 0xFF;
        int green_1 = (rgb >>  8) & 0xFF;
        int blue_1  = (rgb >>  0) & 0xFF;

        rgb         = picture.getRGB(x2, y2);
        int red_2   = (rgb >> 16) & 0xFF;
        int green_2 = (rgb >>  8) & 0xFF;
        int blue_2  = (rgb >>  0) & 0xFF;

        return Math.pow(red_1 - red_2, 2)
                + Math.pow(blue_1 - blue_2, 2)
                + Math.pow(green_1 - green_2, 2);
    }

    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam

        // transpose the image if not done so already
        if (!transposed) {
            transposePicture();
        }

        int path[] = findSeam(); //don't transpose, the above takes care of that logic

        return path;
    }

    public int[] findVerticalSeam() {
        
        if (transposed) {
            transposePicture();
        }
        
        return findSeam();
    }
    
    /**
     * Find the vertical seam
     * @param transpose if true then transpose the image if needed
     * @return 
     */
    private int[] findSeam() {

        int width = picture.width();
        int height = picture.height();
        
        double[][] energy = new double[width][height];
        double[][] distTo = new double[width][height];
        Integer[][] edgeTo = new Integer[width][height];

        for (int i = 0; i < width; i++) {
            Arrays.fill(distTo[i], Double.POSITIVE_INFINITY);
            Arrays.fill(energy[i], ENERGY_INIT_VALUE);
            Arrays.fill(edgeTo[i], null);
        }

        // iterate through each pixel in topological order
        // start at the leftmost column and sweep to the right
        // then go to the next row down
        for (int j = 0; j < height; j++) {

            for (int i = 0; i < width; i++) {

                //initialize top row with zero distance
                if (j == 0) {
                    distTo[i][j] = 0;
                }

                computeEnergy(i, j, energy);

                // relax all edges pointing from this vertex (3)
                // (i-1, j+1), (i, j+1), (i+1,j)
                relaxPixelEdge(i - 1, j + 1, i, j, energy, distTo, edgeTo);
                relaxPixelEdge(i    , j + 1, i, j, energy, distTo, edgeTo);
                relaxPixelEdge(i + 1, j + 1, i, j, energy, distTo, edgeTo);

            }
        }

        //everything is relaxed, find shortest path by checking each vertex
        double minEnergy = Double.POSITIVE_INFINITY;
        Integer minIndex = -1;
        int[] path = new int[height];

        // calculate the min energy path by starting at the bottom of the image
        // for each pixel in the last row
        // this is done based on the relaxed method (edgeTo is populated with 
        // the parent pixel -> row above)
        for (int i = 0; i < width; i++) {

            int j = height - 1;
            double e = energy[i][j]; //start at bottom
            Integer edge = edgeTo[i][j];

            //go through the whole path
            while (edge != null) {
                e += energy[edge][--j];
                edge = edgeTo[edge][j];
            }

            if (minEnergy > e) {
                minEnergy = e;
                minIndex = i;
            }
        }

        // found the min vertex at the bottom of the image
        // provide the min path in order
        for (int j = height - 1; j >= 0; j--) {
            path[j] = minIndex;
            minIndex = edgeTo[minIndex][j];
        }

        return path;
    }

    /**
     * Relax an edge if the conditions are met. See Lecture Notes "Shortest Path
     * Properties" p. 20
     *
     * @param child_col
     * @param child_row
     * @param parent_col
     * @param parent_row
     * @param energy
     * @param distTo
     * @param edgeTo
     */
    private void relaxPixelEdge(int child_col, int child_row, int parent_col,
            int parent_row, double energy[][], double[][] distTo, Integer[][] edgeTo) {

        if (checkIfIndicesValid(child_col, child_row)) {
            
            // calculate energy if we haven't already
            computeEnergy(child_col, child_row, energy);

            if (distTo[child_col][child_row] > distTo[parent_col][parent_row]
                    + energy[child_col][child_row]) {

                // need to relax
                distTo[child_col][child_row] = distTo[parent_col][parent_row]
                        + energy[child_col][child_row];
                
                edgeTo[child_col][child_row] = parent_col;
            }
        }
    }

    public void removeHorizontalSeam(int[] seam) {

        // transpose the picture
        if(!transposed) {
            transposePicture();
        }
        
        removeSeam(seam);
    }

    public void removeVerticalSeam(int[] seam) {

        if(transposed) {
            transposePicture();
        }
        
        removeSeam(seam);
    }
    
    /**
     * Remove a vertical seam.
     * 
     * @param seam 
     */
    private void removeSeam(int[] seam) {
        
        if (seam == null) {
            throw new java.lang.IllegalArgumentException();
        }

        if (seam.length != picture.height()) {
            throw new java.lang.IllegalArgumentException();
        }

        if (picture.width() <= 1) {
            throw new java.lang.IllegalArgumentException();
        }
        
        int newWidth = picture.width() - 1;
        Picture p = new Picture(newWidth, picture.height());
        int lastSeamIndex = -1;
        
        // remove vertical seam from current picture
        for (int j = 0; j < picture.height(); j++) {

            //check entry
            int toRemove = seam[j];
            checkIndices(toRemove, j); //throw if input j is invalid

            //check distance of seam pixel
            if(lastSeamIndex != -1 && Math.abs(lastSeamIndex - toRemove) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
            lastSeamIndex = toRemove;

            int index = 0;
            for (int i = 0; i < newWidth; i++) {
                if (i == toRemove) {
                    index++; //ignore this pixel
                }
                p.setRGB(i, j, picture.getRGB(index++, j));
                //TODO recompute energy?!?!?!?!
            }
        }
        picture = p;
    }

    /**
     * Transpose the picture.
     */
    private void transposePicture() {

        Picture transpose = new Picture(picture.height(), picture.width());

        for (int j = 0; j < picture.height(); j++) {
            for (int i = 0; i < picture.width(); i++) {
                transpose.setRGB(j, i, picture.getRGB(i, j));
            }
        }

        // flip the transposed flag
        picture = new Picture(transpose);
        transposed = !transposed;
    }

    public static void main(String[] args) {

        String fileName = args[0];
        SeamCarver sc = new SeamCarver(new Picture(fileName));

        int[] path = sc.findVerticalSeam();
        System.out.println(Arrays.toString(path));

        sc.removeVerticalSeam(path);
        System.out.println(sc.width());
        System.out.println(sc.height());

        path = sc.findHorizontalSeam();
        System.out.println(Arrays.toString(path));
    }
}
