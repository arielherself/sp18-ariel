package hw2;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private final int[] x;
    private final int T;

    public PercolationStats(int N, int T, PercolationFactory pf) throws IllegalArgumentException {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        this.T = T;
        x = new int[T];
        for (int t = 0; t < T; ++t) {
            Percolation p = pf.make(N);

            ArrayList<Integer> candidates = (ArrayList<Integer>) IntStream.range(0, N * N).boxed().collect(Collectors.toList());

            while (!p.percolates()) {
                final int index = StdRandom.uniform(0, candidates.size());
                final int candidateX = candidates.get(index) / N;
                final int candidateY = candidates.get(index) % N;
                p.open(candidateX, candidateY);
                candidates.remove(index);
            }

            x[t] = p.numberOfOpenSites() / (N * N);
        }
    }

    public double mean() {
        return StdStats.mean(x);
    }

    public double stddev() {
        return StdStats.stddev(x);
    }

    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }
}
