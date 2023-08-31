package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug, Ariel
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private final int sourceX, sourceY, targetX, targetY;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> queue = new LinkedList<>();
        int curV = maze.xyTo1D(sourceX, sourceY);
        final int target = maze.xyTo1D(targetX, targetY);
        queue.add(curV);
        distTo[curV] = 0;
        edgeTo[curV] = curV;
        while (!queue.isEmpty()) {
            curV = queue.poll();
            marked[curV] = true;
            if (target == curV) {
                announce();
                return ;
            }
            for (int vertex : maze.adj(curV)) {
                if (!marked[vertex]) {
                    queue.add(vertex);
                    edgeTo[vertex] = curV;
                    distTo[vertex] = distTo[curV] + 1;
                }
            }
            announce();
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

