/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private final boolean[] open;
    private int openCount;

    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull;

    private final int virtualTop;
    private final int virtualBottom;


    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }

        this.n = n;
        this.open = new boolean[n * n];
        this.openCount = 0;
        this.virtualTop = n * n;
        this.virtualBottom = n * n + 1;
        this.uf = new WeightedQuickUnionUF(n * n + 2);
        this.ufFull = new WeightedQuickUnionUF(n * n + 1);
    }

    private void validate(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new IllegalArgumentException("index out of bounds");
        }
    }

    private int index(int row, int col) {
        return row * n + col;
    }

    private void connectIfOpen(int row, int col, int id) {
        if (row < 0 || row >= n || col < 0 || col >= n) return;

        int neighborId = index(row, col);
        if (open[neighborId]) {
            uf.union(id, neighborId);
            ufFull.union(id, neighborId);
        }
    }

    /**
     * Open the site (row, col) if not open already.
     *
     * @param row row
     * @param col column
     */
    public void open(int row, int col) {
        validate(row, col);
        int id = index(row, col); // convert to 1D id

        if (open[id]) return;

        open[id] = true;
        openCount++;

        // connect to virtual top.
        if (row == 0) {
            uf.union(id, virtualTop);
            ufFull.union(id, virtualTop);
        }

        // connect to virtual bottom only in uf
        if (row == n - 1) {
            uf.union(id, virtualBottom);
        }

        // connect to open neighbors
        connectIfOpen(row - 1, col, id); // up
        connectIfOpen(row + 1, col, id); // down
        connectIfOpen(row, col - 1, id); // left
        connectIfOpen(row, col + 1, id); // right
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);

        return open[index(row, col)];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        int id = index(row, col);

        return open[id] && ufFull.find(id) == ufFull.find(virtualTop);
    }

    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(0, 1);
        p.open(1, 1);
        p.open(0, 2);
        p.open(2, 1);
        p.open(2, 2);
        p.open(2, 0);
        System.out.println(p.percolates()); // true - percolates 👍
    }
}
