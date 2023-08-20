package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

record Point(int x, int y) {
}

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private HexBlock[] blocks = new HexBlock[19];
    private Point[] lu_position = new Point[19];
    private TETile[][] tiles;
    private final int minWidth, maxWidth, maxHeight;
    private final int graphWidth, graphHeight, horizontalMargin, verticalMargin;
    private Random random;
    private TETile[] choices = {Tileset.GRASS, Tileset.FLOOR, Tileset.FLOWER, Tileset.SAND,
            Tileset.LOCKED_DOOR, Tileset.MOUNTAIN, Tileset.PLAYER, Tileset.TREE, Tileset.UNLOCKED_DOOR,
            Tileset.WALL, Tileset.WATER};
    private TERenderer ter;

    public HexWorld(int size) {
        minWidth = size;
        maxWidth = 3 * size - 2;
        maxHeight = size * 2;
        horizontalMargin = minWidth;
        verticalMargin = 1;
        graphWidth = 3 * maxWidth + 2 * minWidth + 2 * horizontalMargin;
        graphHeight = 5 * maxHeight + 2 * verticalMargin;

        ter = new TERenderer();
        ter.initialize(graphHeight, graphWidth);

        for (int i = 0; i < 19; ++i) {
            blocks[i] = new HexBlock(minWidth);
        }

        final int horizontalTab = 2 * size - 1, verticalTab = size, horizontalSpace = size;
        lu_position[0] = new Point(verticalMargin, horizontalMargin + horizontalTab * 2);
        lu_position[1] = new Point(verticalMargin + verticalTab, horizontalMargin + horizontalTab);
        lu_position[2] = new Point(verticalMargin + verticalTab, horizontalMargin + horizontalTab + maxWidth + horizontalSpace);
        lu_position[3] = new Point(verticalMargin + verticalTab * 2, horizontalMargin);
        lu_position[4] = new Point(verticalMargin + verticalTab * 2, horizontalMargin + maxWidth + horizontalSpace);
        lu_position[5] = new Point(verticalMargin + verticalTab * 2, horizontalMargin + maxWidth * 2 + horizontalSpace * 2);
        lu_position[6] = new Point(verticalMargin + verticalTab + maxHeight, horizontalMargin + horizontalTab);
        lu_position[7] = new Point(verticalMargin + verticalTab + maxHeight, horizontalMargin + horizontalTab + maxWidth + horizontalSpace);
        lu_position[8] = new Point(verticalMargin + verticalTab * 2 + maxHeight, horizontalMargin);
        lu_position[9] = new Point(verticalMargin + verticalTab * 2 + maxHeight, horizontalMargin + maxWidth + horizontalSpace);
        lu_position[10] = new Point(verticalMargin + verticalTab * 2 + maxHeight, horizontalMargin + maxWidth * 2 + horizontalSpace * 2);
        lu_position[11] = new Point(verticalMargin + verticalTab + maxHeight * 2, horizontalMargin + horizontalTab);
        lu_position[12] = new Point(verticalMargin + verticalTab + maxHeight * 2, horizontalMargin + horizontalTab + maxWidth + horizontalSpace);
        lu_position[13] = new Point(verticalMargin + verticalTab * 2 + maxHeight * 2, horizontalMargin);
        lu_position[14] = new Point(verticalMargin + verticalTab * 2 + maxHeight * 2, horizontalMargin + maxWidth + horizontalSpace);
        lu_position[15] = new Point(verticalMargin + verticalTab * 2 + maxHeight * 2, horizontalMargin + maxWidth * 2 + horizontalSpace * 2);
        lu_position[16] = new Point(verticalMargin + verticalTab + maxHeight * 3, horizontalMargin + horizontalTab);
        lu_position[17] = new Point(verticalMargin + verticalTab + maxHeight * 3, horizontalMargin + horizontalTab + maxWidth + horizontalSpace);
        lu_position[18] = new Point(verticalMargin + maxHeight * 4, horizontalMargin + horizontalTab * 2);

        tiles = new TETile[graphWidth][graphHeight];
    }

    public void init(int seed) {
        random = new Random(seed);
    }

    public void placeBlock(int position, HexBlock block) {
        final int x_disp = lu_position[position].x();
        final int y_disp = lu_position[position].y();
        for (int i = 0; i < block.height; ++i) {
            for (int j = 0; j < block.width; ++j) {
                if (block.data[i][j] != null) {
                    tiles[y_disp + j][x_disp + i] = block.data[i][j];
                }
            }
        }
    }

    public void fillAllBlocksWithRandom() {
        for (int i = 0; i < 19; ++i) {
            blocks[i].fill(choices[random.nextInt(choices.length)]);
        }
    }

    public void show() {
        for (int i = 0; i < 19; ++i) {
            placeBlock(i, blocks[i]);
        }
        for (int i = 0; i < graphHeight; ++i ) {
            for (int j = 0 ; j < graphWidth; ++j) {
                if (tiles[j][i] == null) {
                    tiles[j][i] = Tileset.NOTHING;
                }
            }
        }
        ter.renderFrame(tiles);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < graphHeight; ++i) {
            for (int j = 0; j < graphWidth; ++j) {
                sb.append(tiles[j][i]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        HexWorld world = new HexWorld(4);
        world.init(825);
        world.fillAllBlocksWithRandom();
        world.show();
    }
}
