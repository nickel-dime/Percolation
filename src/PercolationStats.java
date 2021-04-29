import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] results;
    private final double trial;
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Error: You must have positive trials or a positive grid.");
        }

        trial = trials;


        results = new double[trials];

        for (int i = 0; i < trials; i++) {
            double total = n * n;
            Percolation percolation = new Percolation(n);


            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n);
                int column = StdRandom.uniform(n);
                if (!percolation.isOpen(row+1, column+1)) {
                    percolation.open(row + 1, column + 1);
                }
            }


            results[i] = percolation.numberOfOpenSites()/(total);
        }

        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);


    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence intervajl
    public double confidenceLo() {
        return mean - ((1.96 * (stddev()))/(Math.sqrt(trial)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + ((1.96 * (stddev()))/(Math.sqrt(trial)));
    }

    private void printToConsole(int n, int t) {
        PercolationStats percolationStats = new PercolationStats(n, t);
        StdOut.println("mean                     = " + percolationStats.mean());
        StdOut.println("stddev                   = " + percolationStats.stddev());
        StdOut.println("95% confidence interval  = " + "[" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");

    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats perc = new PercolationStats(n, t);

        perc.printToConsole(n, t);
    }


}

