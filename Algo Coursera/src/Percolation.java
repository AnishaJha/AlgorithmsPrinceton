import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private final int size;
    private int numOpen;
    private final WeightedQuickUnionUF uf;
    private final int top;
    private final int bottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("index" + n + "is <=0");
        grid = new boolean[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                grid[i][j] = false;
        uf = new WeightedQuickUnionUF(n * n + 2);
        size = n;
        numOpen = 0;
        top = 0;
        bottom = n * n + 1;
    }

    private void validate(int num) {
        if (num < 1 || num > size)
            throw new IllegalArgumentException("index" + num + "is not between 1 and" + size);
    }

    private int findUFIndex(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size)
            return -1;
        return (row - 1) * size + col;
    }

    private int gridIndex(int num) {
        return num - 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row);
        validate(col);
        if (grid[gridIndex(row)][gridIndex(col)])
            return;
        grid[gridIndex(row)][gridIndex(col)] = true;
        int currIndex = findUFIndex(row, col);
        int rowUp = findUFIndex(row - 1, col);
        int rowDown = findUFIndex(row + 1, col);
        int colLeft = findUFIndex(row, col - 1);
        int colRight = findUFIndex(row, col + 1);
        if (rowUp != -1) {
            if (grid[gridIndex(row - 1)][gridIndex(col)])
                uf.union(currIndex, rowUp);
        } else {
            uf.union(currIndex, top);
        }
        if (rowDown != -1) {
            if (grid[gridIndex(row + 1)][gridIndex(col)])
                uf.union(currIndex, rowDown);
        } else {
            uf.union(currIndex, bottom);
        }
        if (colLeft != -1) {
            if (grid[gridIndex(row)][gridIndex(col - 1)])
                uf.union(currIndex, colLeft);
        }
        if (colRight != -1) {
            if (grid[gridIndex(row)][gridIndex(col + 1)])
                uf.union(currIndex, colRight);
        }
        numOpen++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row);
        validate(col);
        return grid[gridIndex(row)][gridIndex(col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row);
        validate(col);
        return uf.connected(findUFIndex(row, col), top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(top, bottom);
    }

    // test client (optional)

    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int n = in.readInt();
        Percolation perc = new Percolation(n);
        int count = 0;
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
            count++;
            System.out.println("Line no+" + count + "val" + i + "," + j);
            for (int k = 1; k < n + 1; k++) {
                for (int l = 1; l < n + 1; l++) {
                    if (perc.isFull(k, l))
                        System.out.print("Is Full" + k + l + perc.isFull(k, l) + " ");
                    if (perc.isOpen(k, l))
                        System.out.print("Is Open" + k + l + perc.isOpen(k, l) + " ");
                }

                System.out.println(" ");
            }
        }
        /*
        Percolation p = new Percolation(4);

        System.out.println("Open 0,0");
        p.open(1, 1);
        System.out.println("Open 2,0");
        p.open(3, 1);

        System.out.println("Open 2,1");
        p.open(3, 2);
        System.out.println("Open 3,1");
        p.open(4, 2);
        System.out.println("Open 3,1");
        p.open(2, 1);

        System.out.println("p isopen 2,2?" + p.isOpen(2, 2));
        System.out.println("p percolates?" + p.percolates());
        System.out.println("Number of open sites" + p.numberOfOpenSites());

         */
    }
}
