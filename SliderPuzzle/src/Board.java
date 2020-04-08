import java.util.Arrays;
import java.util.Iterator;

public class Board {

    private int[][] tiles;
    private int n;
    private int row0;
    private int col0;
    private int hamming;
    private int manhattan;
    private Board[] neighbor;
    private int neighborCount;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    public Board(int[][] tiles){
        n = tiles.length;
        hamming = 0;
        manhattan = 0;
        if (n < 2 || n >=128) {
            throw new IllegalArgumentException("Length of input tiles is not correct: " + n);
        }

        this.tiles = new int[n][n];
        // arrayCopy2d(tiles, this.tiles);
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                this.tiles[i][j]=tiles[i][j];
                if(tiles[i][j] == 0){
                    row0 = i;
                    col0 = j;
                }
            }
        }

    }

    private void arrayCopy2d(int[][] arr1, int[][] arr2) {
        int arrlen = arr1.length;
        for (int i = 0; i < arrlen; i++){
            System.arraycopy(arr1[i], 0, arr2[i], 0, arrlen);
        }
    }

    private void swap(int[][] tempneighbor, int i, int j, int i1, int j1) {
        int temp = tempneighbor[i][j];
        tempneighbor[i][j] = tempneighbor[i1][j1];
        tempneighbor[i1][j1] = temp;
    }

    // string representation of this board
    public String toString(){
        StringBuilder myBoard = new StringBuilder(Integer.toString(n) + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                myBoard.append(" " + Integer.toString(tiles[i][j]));
            }
            myBoard.append("\n");
        }
        return myBoard.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int currDist;
        if(hamming == 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    currDist = i * n + j + 1;
                    if ((currDist != tiles[i][j]) && tiles[i][j] != 0) hamming++;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if(manhattan == 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int currIndex = tiles[i][j];
                    if (currIndex != 0) {
                        int row = (currIndex - 1) / n;
                        int col = (currIndex - 1) - (n * row);
                        manhattan = manhattan + Math.abs(row - i) + Math.abs(col - j);
                    }
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if(y == this)
            return true;
        if (y instanceof Board) {
            Board yBoard = (Board) y;
            if (yBoard.dimension() == dimension()) {
               for(int i = 0; i < n; i ++) {
                   for (int j = 0; j < n; j++){
                       if (yBoard.tiles[i][j] != tiles[i][j]) {
                           return false;
                       }
                   }
               }
               return true;
            }
            // return this.toString().equals(((Board) y).toString());
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (neighborCount > 0)
            return new BoardIterable();
        int[][] tempneighbor = new int[n][n];
        neighbor = new Board[4];
        arrayCopy2d(tiles,tempneighbor);
        if ( row0 != 0) {
            swap(tempneighbor, row0, col0, row0-1, col0);
            neighbor[neighborCount++] = new Board(tempneighbor);
            swap(tempneighbor, row0-1, col0, row0, col0);
        }
        if ( row0 != (n-1)) {
            swap(tempneighbor, row0, col0, row0+1, col0);
            neighbor[neighborCount++] = new Board(tempneighbor);
            swap(tempneighbor, row0+1, col0, row0, col0);
        }
        if ( col0 != 0) {
            swap(tempneighbor, row0, col0, row0, col0 - 1);
            neighbor[neighborCount++] = new Board(tempneighbor);
            swap(tempneighbor, row0, col0 - 1, row0, col0);
        }
        if ( col0 != (n-1)) {
            swap(tempneighbor, row0, col0, row0, col0 + 1);
            neighbor[neighborCount++] = new Board(tempneighbor);
            swap(tempneighbor, row0, col0 + 1, row0, col0);
        }
        return new BoardIterable();
    }

    private class BoardIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new BoardIterator();
        }

        private class BoardIterator implements Iterator<Board> {
            int i=0;
            public boolean hasNext() {
                return i < neighborCount;
            }
            public Board next() {
                return neighbor[i++];
            }
        }
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = new int[n][n];
        arrayCopy2d(tiles, twinTiles);
        int prev = twinTiles[0][0];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(j != 0 && prev!= 0 && twinTiles[i][j] != 0) {
                    twinTiles[i][j-1] = twinTiles[i][j];
                    twinTiles[i][j] = prev;
                    return new Board(twinTiles);
                }
                prev = twinTiles[i][j];
            }
        }
        return new Board(twinTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

}
