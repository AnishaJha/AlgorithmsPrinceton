import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Main {

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        Board twinBoard = initial.twin();
        //System.out.println(twinBoard);
        System.out.print("Print neighbors: " );
        int count = 0;
        for(Board board: initial.neighbors())
            count ++;
        System.out.println(count);
        //StdIn.readString();
        System.out.println(initial.equals(twinBoard));
        System.out.println(initial);
        System.out.println("Hamming:" + initial.hamming());
        System.out.println("Manhattan:" + initial.manhattan());

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
