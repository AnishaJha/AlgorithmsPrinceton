import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int numTrials;
    private final double[] threshold;
    private double meanVal;
    private double stddevVal;
    private static final double confidenceInt = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0)
            throw new IllegalArgumentException("Grid size " + n + "<=0");
        if (trials <= 0)
            throw new IllegalArgumentException("Number of trails" + trials + "<=0");
        numTrials = trials;
        threshold = new double[trials];
        Percolation p;
        for (int i = 0; i < trials; i++) {
            p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                p.open(row, col);
            }
            threshold[i] = ((double) p.numberOfOpenSites()) / (n * n);
        }
        meanVal = -1;
        stddevVal = -1;
    }

    // sample mean of percolation threshold
    public double mean() {
        meanVal = StdStats.mean(threshold);
        return meanVal;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        stddevVal = StdStats.stddev(threshold);
        return stddevVal;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        if (meanVal < 0)
            meanVal = mean();
        if (stddevVal < 0)
            stddevVal = stddev();
        return (meanVal - ((confidenceInt * stddevVal) / Math.sqrt(numTrials)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        if (meanVal < 0)
            meanVal = mean();
        if (stddevVal < 0)
            stddevVal = stddev();
        return (meanVal + ((confidenceInt * stddevVal) / Math.sqrt(numTrials)));
    }

    // test client (see below)
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        System.out.println("Size:" + size);
        System.out.println("Trials:" + trials);
        PercolationStats ps = new PercolationStats(size, trials);
        System.out.println("mean =" + ps.mean());
        System.out.println("standard dev =" + ps.stddev());
        System.out.println("95% Confidence Interval = [" + ps.confidenceLo() + "," + ps.confidenceHi() + "]");
    }
}

