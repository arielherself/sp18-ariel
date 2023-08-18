package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private HexBlock[] blocks = new HexBlock[19];
    // TODO: position matrix
    private final int minWidth, maxWidth;
    private final int graphWidth, margin;
    private Random random;
    private TERenderer ter;

    public HexWorld(int size) {
        minWidth = size;
        maxWidth = 3 * size - 2;
        margin = minWidth;
        graphWidth = 3 * maxWidth + 2 * minWidth;

        ter = new TERenderer();
        ter.initialize(graphWidth + margin, graphWidth + margin);

        for (int i = 0; i < 19; ++i) {
            blocks[i] = new HexBlock(minWidth);
        }
    }

    public void init(int seed) {
        random = new Random(seed);
    }


}
