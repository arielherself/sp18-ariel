package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF[] data;
    private final int[] fullPos;
    private final int[] openPos;
    private int fullCount = 0;
    private int openSiteCount = 0;
    private final int size;

    public Percolation(int N) {
        fullPos = new int[N];
        openPos = new int[N];
        data = new WeightedQuickUnionUF[N];
        for (int i = 0; i < N; ++i) {
            fullPos[i] = -1;
            openPos[i] = -1;
            data[i] = new WeightedQuickUnionUF(N);
        }
        size = N;
    }
    public void open(int row, int col) {
        if (openPos[row] == -1) {
            openPos[row] = col;
        } else {
            if (col > 0 && data[row].connected(col - 1, openPos[row])) {
                data[row].union(col, openPos[row]);
            }
            if (col < size - 1 && data[row].connected(col + 1, openPos[row])) {
                data[row].union(col, openPos[row]);
            }
        }
        if (row == 0) {
            if (fullPos[0] == -1) {
                fullPos[0] = col;
                ++fullCount;
            }
        } else {
            final int[] tr = (row != size - 1) ? new int[] {row - 1, row + 1} : new int[] {row - 1};
            for (int t : tr) {
                if (fullPos[t] != -1) {
                    if (data[t].connected(col, fullPos[t]) && fullPos[row] == -1) {
                        fullPos[row] = col;
                        ++fullCount;
                    }
                } else {
                    if (fullPos[row] != -1 && data[row].connected(col, fullPos[row])
                            && openPos[t] != -1 && data[t].connected(col, openPos[t])) {
                        fullPos[t] = col;
                        ++fullCount;
                    }
                }
            }
        }
        ++openSiteCount;
    }
    public boolean isOpen(int row, int col) {
        if (openPos[row] == -1) {
            return false;
        }
        return data[row].connected(col, openPos[row]);
    }
    public boolean isFull(int row, int col) {
        if (fullPos[row] == -1) {
            return false;
        }
        return data[row].connected(col, fullPos[row]);
    }

    public int numberOfOpenSites() { return openSiteCount; }
    public boolean percolates() { return fullCount == size; }
    public static void main(String[] args) {}
}
