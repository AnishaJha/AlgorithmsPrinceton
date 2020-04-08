import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private int size;
    private int num_open;
    private WeightedQuickUnionUF uf;
    private int top;
    private int bottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        if(n<=0)
            throw new IllegalArgumentException("index" + n + "is <=0");
        grid = new boolean[n][n];
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                grid[i][j]=false;
        uf = new WeightedQuickUnionUF(n*n+2);
        size = n;
        num_open=0;
        top=0;
        bottom=n*n+1;

    }
    private void validate(int num){
        if(num<1 || num >size)
            throw new IllegalArgumentException("index" + num + "is not between 1 and" + size);
    }
    private int find_uf_index(int row, int col){
        if(row<1 || row>size || col<1 || col >size)
            return -1;
        return (row-1)*size+col;
    }
    private int grid_index(int num){
        return num-1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        validate(row);
        validate(col);
        if(grid[grid_index(row)][grid_index(col)])
            return;
        grid[grid_index(row)][grid_index(col)]=true;
        int curr_index=find_uf_index(row,col);
        int row_up = find_uf_index(row-1,col);
        int row_down = find_uf_index(row+1, col);
        int col_left = find_uf_index(row,col-1);
        int col_right = find_uf_index(row,col+1);
        // System.out.println("row"+row+"col"+col);
        // System.out.println("row_up"+row_up+"row_down"+row_down+"col_left"+col_left+"col_right"+col_right);
        if(row_up!=-1) {
            if (grid[grid_index(row - 1)][grid_index(col)])
                uf.union(curr_index, row_up);
        }
        else {
            uf.union(curr_index, top);
            // System.out.println(curr_index + "is connected to " + top);
        }
        if(row_down!=-1) {
            if (grid[grid_index(row + 1)][grid_index(col)])
                uf.union(curr_index, row_down);
        }
        else {
            uf.union(curr_index, bottom);
            // System.out.println(curr_index + "is connected to " + bottom);
        }
        if(col_left!=-1) {
            if (grid[grid_index(row)][grid_index(col - 1)])
                uf.union(curr_index, col_left);
        }
        if(col_right!=-1) {
            if (grid[grid_index(row)][grid_index(col + 1)])
                uf.union(curr_index, col_right);
        }
        num_open++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        validate(row);
        validate(col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        validate(row);
        validate(col);
        return uf.connected(find_uf_index(row,col),top);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return num_open;
    }

    // does the system percolate?
    public boolean percolates(){
        return uf.connected(top,bottom);
    }

    public int components(){
        return uf.count();
    }



    // test client (optional)
    public static void main(String[] args){
    Percolation p = new Percolation(5);
    System.out.println("Components" + p.components());
    System.out.println("Open 1,1");
    p.open(1, 2);
    System.out.println("Components" + p.components());
    System.out.println("Open 3,1");
    p.open(3, 1);
    System.out.println("Components" + p.components());

    System.out.println("Open 3,2");
    p.open(3, 2);
    System.out.println("Components" + p.components());

    System.out.println("Open 4,2");
    p.open(4, 2);

    System.out.println("Components" + p.components());

    System.out.println("Open 2,1");
    // p.open(2, 2);
    System.out.println("Open 5,1");
    p.open(5, 2);

    System.out.println("p isopen 2,2?"+p.isOpen(2, 2));
    System.out.println("p percolates?"+p.percolates());
    }
}
