package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;

public class Percolation {
    private final WeightedQuickUnionUF connectivityForPercolates, trueLastLineConnectivity;
    private final boolean[] openStatus;
    private final int N;
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
                connectivityForPercolates.union((N - 1) * N + i - 1, (N - 1) * N + i);
            }
        }
    }

    public void open(int row, int col) {
        if (openStatus[row * N + col]) {
            return ;
        }
        openStatus[row * N + col] = true;
        ++openSiteCount;
        ArrayList<Integer> toConnect = new ArrayList<>();
        if (row > 0) {
            toConnect.add(row * N + col - N);
        }
        if (row < N - 1) {
            toConnect.add(row * N + col + N);
        }
        if (col > 0) {
            toConnect.add(row * N + col - 1);
        }
        if (col < N - 1) {
            toConnect.add(row * N + col + 1);
        }
        for (int c : toConnect) {
            if (openStatus[c]) {
                connectivityForPercolates.union(c, row * N + col);
                trueLastLineConnectivity.union(c, row * N + col);
            }
        }
        if (row == 0) {
            firstLineOpened = true;
        }
    }
    public boolean isOpen(int row, int col) {
        return openStatus[row * N + col];
    }
    public boolean isFull(int row, int col) {
        return openStatus[row * N + col] && trueLastLineConnectivity.connected(row * N + col, 0);
    }

    public int numberOfOpenSites() { return openSiteCount; }
    public boolean percolates() { return firstLineOpened && connectivityForPercolates.connected((N - 1) * N + N - 1, 0); }
    public static void main(String[] args) {}
}
