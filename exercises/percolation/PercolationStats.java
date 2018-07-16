
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 *
 * @author devin
 */
public class PercolationStats {

    private final double mean;
    private final double stddev;
    private final double confidenceHi;
    private final double confidenceLow;

    // perform trials independent experiments on an n-by-n grid 
    public PercolationStats(int n, int trials) {

        if (n <= 0) {
            throw new IllegalArgumentException("n cannot be zero");
        }

        if (trials <= 0) {
            throw new IllegalArgumentException("trials cannot be zero");
        }

        double[] samples = new double[trials];

        for (int k = 0; k < samples.length; k++) {

            Percolation p = new Percolation(n);
            // starts empty
            while (!p.percolates()) {

                // choose a random site to open
                int i = StdRandom.uniform(1, n + 1);
                int j = StdRandom.uniform(1, n + 1);

                p.open(i, j);
            }

            samples[k] = p.numberOfOpenSites() * 1.0 / (n * n);
        }

        mean = StdStats.mean(samples);
        stddev = StdStats.stddev(samples);
        double magicNumber = 1.96; // could be static final
        confidenceLow = mean - (magicNumber * stddev) / Math.sqrt(samples.length);
        confidenceHi = mean + (magicNumber * stddev) / Math.sqrt(samples.length);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLow;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, t);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo()
                + ", " + ps.confidenceHi() + "]");

    }
}
