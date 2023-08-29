package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Solver {
    private final ArrayList<WorldState> solution;
    private final int moves;

    private static class SearchNode implements Comparable<SearchNode> {
        private final WorldState state;
        private final int numHistoryMoves;
        private final SearchNode parent;
        private final int totalMoves;

        private SearchNode(WorldState state, int numHistoryMoves, SearchNode parent) {
            this.state = state;
            this.numHistoryMoves = numHistoryMoves;
            this.parent = parent;
            totalMoves = numHistoryMoves + state.estimatedDistanceToGoal();
        }

        private SearchNode userClone() {
            return new SearchNode(state, numHistoryMoves, parent);
        }

        @Override
        public int compareTo(SearchNode o) {
            if (o == null || o.getClass() != getClass()) {
                throw new RuntimeException();
            }
            return totalMoves - o.totalMoves;
        }

        public void toStringHelper(StringBuilder sb) {
            if (parent != null) {
                parent.toStringHelper(sb);
            }
            sb.append(state + " ");
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("SearchNode[");
            toStringHelper(sb);
            sb.append("]");
            return sb.toString();
        }
    }

    private static SearchNode solve(MinPQ<SearchNode> pq) {
        do {
            SearchNode f = pq.delMin();
            if (f.state.isGoal()) {
                return f;
            }
            e: for (WorldState neighbor : f.state.neighbors()) {
                if (f.parent != null && neighbor.equals(f.parent.state)) {
                    continue;
                }
                SearchNode newNode = new SearchNode(neighbor, f.numHistoryMoves + 1, f);
                pq.insert(newNode);
            }
        } while (true);

    }

    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(new SearchNode(initial, 0, null));
        SearchNode n = solve(pq);
        ArrayDeque<WorldState> deque = new ArrayDeque<>();
        while (n != null) {
            deque.addFirst(n.state);
            n = n.parent;
        }
        solution = new ArrayList<>();
        solution.addAll(deque);
        moves = solution.size() - 1;
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        return solution;
    }
}
