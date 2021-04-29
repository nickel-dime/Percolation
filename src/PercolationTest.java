import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 *
 * @author hharwani
 * @description the percolation problem with backwash solved.
 * the backwash problem was that some of the bottom sites were incorrectly getting detected as full,
 * this was happening because of two virtual sites top and bottom, as we were checking if top and virtual
 * sites are connected or not when they got connected these sites were incorrectly marked as full as they were
 * connected with the bottom virtual site. So in order to solve this problem we maintain another union-find
 * instance and in that instance no virtual bottom site is maintained, only virtual top site is maintained
 * and isfull checks if the site is actually connected to top site.
 *
 */

public class PercolationTest {

    private WeightedQuickUnionUF wuf; // weighted quick find object reference
    private boolean[] openSiteGrid; // a boolean array for holding whether a particular site is open or close.
    private int size; // size of the grid

    public PercolationTest(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.size = N; // size of the grid.
        wuf = new WeightedQuickUnionUF((N * N) + 2); // two extra site for virtual top and bottom site
        openSiteGrid = new boolean[(N * N) + 2]; // create N-by-N grid, with all sites blocked
    }

    private int getIndexOfElementinGrid(int i, int j) {
        if (isIndicesInvalid(i, j)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return (i - 1) * this.size + j; //the two virtual sites will have index  0 and N*N+1 respectively
    }

    /**
     * @param i
     * @param j
     * @return boolean
     * @description checks if the indices are in valid range or not
     */
    private boolean isIndicesInvalid(int i, int j) {
        return (i < 1 || i > this.size || j < 1 || j > this.size);
    }

    /**
     * @return void
     * @param i
     * @param j
     * @description this method connects two sites
     */
    private void connect(int i, int j) {
        wuf.union(i, j);
    }


    public void open(int i, int j) {
        if (isIndicesInvalid(i, j)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int index = getIndexOfElementinGrid(i, j);
        if (!openSiteGrid[index]) {
            openSiteGrid[index] = true; // open site (row i, column j) if it is not open already
        }
        if (i == 1) {
            connect(index, 0);
        }
        if (i == this.size) {
            wuf.union(index, (this.size * this.size) + 1);
        }
        if (i > 1 && isOpen(i - 1, j)) { // if the left site is open connect it to left site
            connect(index, getIndexOfElementinGrid(i - 1, j));
        }
        if (i < this.size && isOpen(i + 1, j)) { // if the bottom site is open connect it to bottom site
            connect(index, getIndexOfElementinGrid(i + 1, j));
        }
        if (j > 1 && isOpen(i, j - 1)) { // if the left site is open connect it to left site
            connect(index, getIndexOfElementinGrid(i, j - 1));
        }
        if (j < this.size && isOpen(i, j + 1)) { // if the right site is open connect it to right site
            connect(index, getIndexOfElementinGrid(i, j + 1));
        }
    }


    public boolean isOpen(int i, int j) {
        if (isIndicesInvalid(i, j)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int index = getIndexOfElementinGrid(i, j);
        return openSiteGrid[index];
    }

    public boolean isFull(int i, int j) {
        if (isIndicesInvalid(i, j)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int index = getIndexOfElementinGrid(i, j);
        return wuf.connected(index, 0);
    }



    // returns the number of open sites
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < openSiteGrid.length; i++) {
                if (openSiteGrid[i] == true) {
                    count += 1;
                }
            }

        return count;
    }


    /**
     * @return boolean
     * @description checks whether the system percolates or not
     */
    public boolean percolates() {
        return wuf.connected((this.size * this.size) + 1, 0);
    }

}
