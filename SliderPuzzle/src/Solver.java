import edu.princeton.cs.algs4.MinPQ;
import java.util.Iterator;

public class Solver {
    private int moves;
    //private Board[] solution;
    private Board initial;
    private boolean solvable = false;
    private searchNode solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        moves = -1;
        this.initial = initial;
        if ( !isSolvable() ) {
            System.out.println("Board is not solvable");
        }

    }

    private boolean SolveBoard(MinPQ<searchNode> pq) {
        searchNode currNode  = pq.delMin();
        // System.out.println("currNode" + currNode.board);
        if (currNode.board.isGoal() ) {
            // System.out.println("Reached Goal");
            solution = currNode.reverseNode();
            moves = currNode.moves;
            return true;
        }
        int moves = currNode.moves + 1;

        Iterable<Board> neighbors = currNode.board.neighbors();
        // System.out.println("neighbors");
        for(Board board: neighbors) {

            if (currNode.prev == null || !board.equals(currNode.prev.board)) {
                pq.insert(new searchNode(board, moves, currNode));
            }
        }
        // StdIn.readString();
        return false;
    }

    private class searchNode implements Comparable<searchNode> {
        public int manhattan;
        public int moves;
        public searchNode prev;
        public Board board;

        public searchNode(Board board, int moves, searchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.manhattan = board.manhattan();
        }

        private int priority() {
            return moves + manhattan;
        }

        public int compareTo(searchNode that) {
            return Integer.compare(this.priority(), that.priority());
        }

        public searchNode reverseNode() {
            searchNode temp = this, next = this.prev, next1;
            this.prev = null;
            while (next != null) {
                next1 = next.prev;
                next.prev = temp;
                temp = next;
                next = next1;
            }
            return temp;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (moves != -1)
            return solvable;
        Board twinBoard = initial.twin();
        searchNode initialNode = new searchNode(initial, 0, null);
        MinPQ<searchNode> pq = new MinPQ<>(5);
        pq.insert(initialNode);
        searchNode twinNode = new searchNode(twinBoard, 0, null);
        MinPQ<searchNode> pqTwin = new MinPQ<>(5);
        pqTwin.insert(twinNode);
        boolean solvedInit, solvedTwin;

        while (true) {
            solvedInit = SolveBoard(pq);
            // System.out.println("solvedInit" + solvedInit);
            if (solvedInit) {
                solvable = true;
                break;
            }
            solvedTwin = SolveBoard(pqTwin);
            if (solvedTwin) {
                solvable = false;
                break;
            }
        }

        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return new BoardIterable();
    }

    private class BoardIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new BoardIterator();
        }

        private class BoardIterator implements Iterator<Board> {
            searchNode sol=solution;
            public boolean hasNext() {
                return sol != null;
            }
            public Board next() {
                Board cur = sol.board;
                sol = sol.prev;
                return cur;
            }
        }
    }

    // test client (see below)
    public static void main(String[] args) {

    }

}
