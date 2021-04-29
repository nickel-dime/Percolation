import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] site;
    private final int size;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf2;
    private int count;



    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Error: You must have a grid with positive rows and columns.");
        }

        size = n;
        count = 0;

        uf = new WeightedQuickUnionUF((n*n)+2);
        uf2 = new WeightedQuickUnionUF((n*n)+1);
        for (int i = 1; i <= n; i++) {
            uf.union(i, 0);
            uf2.union(i,0);
        }
        for (int i = (n*n); i > ((n*n)-n); i--) {
            uf.union(((n*n)+1), i);
        }


        site = new boolean[n][n];
        for (int i = 0; i < site.length; i++) {
            for (int p = 0; p < site.length; p++) {
                site[i][p] = false;
            }
        }

    }

    private boolean validate(int row, int col) {
        return row <= size && col <= size && row > 0 && col > 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || col < 1 || row > size || col > size) {
            throw new IllegalArgumentException("Error: the open method was given " + row + " " + col + ", which was outside it's range");
        }
        // set value to open
        if (!site[row - 1][col - 1]) {
            site[row-1][col-1] = true;
            count += 1;
        }

        // find index value of adjacent sites
        int valueSite = ((row-1) * size) + col;
        int valueAbove = valueSite-size;
        int valueBelow = valueSite+size;
        int valueRight = valueSite+1;
        int valueLeft = valueSite-1;

        // if adjacent sites are open, connect them to newly opened site
        if (validate(row+1, col)) {
            if (isOpen(row+1, col)) {
                uf.union(valueSite, valueBelow);
                uf2.union(valueSite, valueBelow);
            }
        }
        if (validate(row-1, col)) {
            if (isOpen(row-1, col)) {
                uf.union(valueSite, valueAbove);
                uf2.union(valueSite, valueAbove);
            }
        }
        if (validate(row, col+1)) {
            if (isOpen(row, col+1)) {
                uf.union(valueSite, valueRight);
                uf2.union(valueSite, valueRight);
            }
        }
        if (validate(row, col-1)) {
            if (isOpen(row, col-1)) {
                uf.union(valueSite, valueLeft);
                uf2.union(valueSite, valueLeft);
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || col < 1 || row > size || col > size) {
            throw new IllegalArgumentException("Error: the isOpen method was given " + row + " " + col + ", which was outside it's range");
        }
        return site[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || col < 1 || row > size || col > size) {
            throw new IllegalArgumentException("Error: the isFull method was given " + row + " " + col + ", which was outside it's range");
        }

        int value = ((row-1) * size) + col;
        if (isOpen(row, col)) {
            return (uf2.find(0) == uf2.find(value));
        }
        return false;

    }



    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        if (size == 1) {
            return numberOfOpenSites() == 1;
        }
        return (uf.find(0) == uf.find((size*size)+1));
    }

}
