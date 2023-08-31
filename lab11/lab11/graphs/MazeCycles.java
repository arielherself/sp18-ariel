package lab11.graphs;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private final WeightedQuickUnionUF qu = new WeightedQuickUnionUF(maze.V());

    public MazeCycles(Maze m) {
        super(m);
    }

    public void dfs(int v) {
        marked[v] = true; // preorder dfs
        for (int child : maze.adj(v)) {
            edgeTo[child] = v;
            if (marked[child]) {
                if (qu.connected(child, v)) {
                    announce();
                    return ;
                } else {
                    qu.union(child, v);
                }
            } else {
                dfs(child);
            }
        }
        announce();
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        dfs(0);
    }

    // Helper methods go here
}

