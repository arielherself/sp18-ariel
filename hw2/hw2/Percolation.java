package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;

public class Percolation {
    private final WeightedQuickUnionUF connectivityForPercolates, trueLastLineConnectivity;
    private final boolean[] openStatus;
    final int N;
    private int openSiteCount = 0;
    private boolean firstLineOpened = false;

    public Percolation(int N) {
        this.N = N;
        openStatus = new boolean[N * N];
        trueLastLineConnectivity = new WeightedQuickUnionUF(N * N);
        connectivityForPercolates = new WeightedQuickUnionUF(N * N);
        for (int i = 0; i < N; ++i) {
            if (i != 0) {
                trueLastLineConnectivity.union(i - 1, i);
                connectivityForPercolates.union(i - 1, i);
                connectivityForPercolates.union(expandIndex(N - 1, i - 1), expandIndex(N - 1, i));
            }
        }
    }

    public int expandIndex(int x, int y) {
        return x * N + y;
    }

    public int getAboveIndex(int expandedIndex) {
        return expandedIndex - N;
    }

    public int getBelowIndex(int expandedIndex) {
        return expandedIndex + N;
    }

    public void open(int row, int col) {
        if (openStatus[expandIndex(row, col)]) {
            return ;
        }
        openStatus[expandIndex(row, col)] = true;
        ++openSiteCount;
        ArrayList<Integer> toConnect = new ArrayList<>();
        if (row > 0) {
            toConnect.add(getAboveIndex(expandIndex(row, col)));
        }
        if (row < N - 1) {
            toConnect.add(getBelowIndex(expandIndex(row, col)));
        }
        if (col > 0) {
            toConnect.add(expandIndex(row, col - 1));
        }
        if (col < N - 1) {
            toConnect.add(expandIndex(row, col + 1));
        }
        for (int c : toConnect) {
            if (openStatus[c]) {
                connectivityForPercolates.union(c, expandIndex(row, col));
                trueLastLineConnectivity.union(c, expandIndex(row, col));
            }
        }
        if (row == 0) {
            firstLineOpened = true;
        }
    }
    public boolean isOpen(int row, int col) {
        return openStatus[expandIndex(row, col)];
    }
    public boolean isFull(int row, int col) {
        return openStatus[expandIndex(row, col)] && trueLastLineConnectivity.connected(expandIndex(row, col), 0);
    }

    public int numberOfOpenSites() { return openSiteCount; }
    public boolean percolates() { return firstLineOpened && connectivityForPercolates.connected(expandIndex(N - 1, N - 1), 0); }
    public static void main(String[] args) {}
}
