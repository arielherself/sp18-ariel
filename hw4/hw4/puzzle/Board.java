package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.LinkedList;

public class Board implements WorldState {
    private final int[][] tiles;
    private final int size;

    public Board(int[][] tiles) {
        this.tiles = copy(tiles);
        this.size = tiles.length;
    }

    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    public int size() {
        return size;
    }

    private static int[][] copy(int[][] array) {
        final int sizeX = array.length, sizeY = array[0].length;
        int[][] newArray = new int[sizeX][sizeY];
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                newArray[i][j] = array[i][j];
            }
        }
        return newArray;
    }

    private int[][] copy() {
        return copy(tiles);
    }

    public Iterable<WorldState> neighbors() {
        int zeroX = -1, zeroY = -1;
        c: for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (tiles[i][j] == 0) {
                    zeroX = i;
                    zeroY = j;
                    break c;
                }
            }
        }
        LinkedList<WorldState> result = new LinkedList<>();
        if (zeroY < size - 1) {
            int[][] a = copy();
            a[zeroX][zeroY] ^= a[zeroX][zeroY + 1];
            a[zeroX][zeroY + 1] ^= a[zeroX][zeroY];
            a[zeroX][zeroY] ^= a[zeroX][zeroY + 1];
            result.add(new Board(a));
        }
        if (zeroY > 0) {
            int[][] a = copy();
            a[zeroX][zeroY] ^= a[zeroX][zeroY - 1];
            a[zeroX][zeroY - 1] ^= a[zeroX][zeroY];
            a[zeroX][zeroY] ^= a[zeroX][zeroY - 1];
            result.add(new Board(a));
        }
        if (zeroX < size - 1) {
            int[][] a = copy();
            a[zeroX][zeroY] ^= a[zeroX + 1][zeroY];
            a[zeroX + 1][zeroY] ^= a[zeroX][zeroY];
            a[zeroX][zeroY] ^= a[zeroX + 1][zeroY];
            result.add(new Board(a));
        }
        if (zeroX > 0) {
            int[][] a = copy();
            a[zeroX][zeroY] ^= a[zeroX - 1][zeroY];
            a[zeroX - 1][zeroY] ^= a[zeroX][zeroY];
            a[zeroX][zeroY] ^= a[zeroX - 1][zeroY];
            result.add(new Board(a));
        }
        return result;
    }

    public int hamming() {
        int count = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if ((i != size - 1 || j != size - 1) && tiles[i][j] != i * size + j + 1) {
                    ++count;
                }
            }
        }
        return count;
    }

    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (tiles[i][j] != 0) {
                    distance += Math.abs(i - (tiles[i][j] - 1) / size) + Math.abs(j - (tiles[i][j] - 1) % size);
                }
            }
        }
        return distance;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (y == null || y.getClass() != getClass()) {
            return false;
        }
        Board z = (Board) y;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (tiles[i][j] != z.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
