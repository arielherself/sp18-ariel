package byog.util;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.time.LocalTime;
import java.util.Random;

public class World extends MirrorCompatible<TETile> {
    private final TETile[][] world;
    private final int height, width;

    @Override
    public TETile[][] getData() {
        return world;
    }

    public World(int height, int width) {
        this.height = height;
        this.width = width;
        world = (TETile[][]) new Object[height][width];
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    public boolean checkMergeability(ElementBase<TETile> element) {
        for (int i = 0; i < element.height; ++i) {
            for (int j = 0; j < element.width; ++j) {
                if (world[element.positionX + i][element.positionY + j] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    public void merge(ElementBase<TETile> element) {
        assert checkMergeability(element);
        for (int i = 0; i < element.height; ++i) {
            System.arraycopy(element.data[i], 0, world[element.positionX + i], element.positionY, element.width);
        }
    }

    public boolean hasHorizontalSpaceAround(int x, int y) {
        return world[x][y] == Tileset.NOTHING &&
                world[x-1][y] == Tileset.NOTHING &&
                world[x+1][y] == Tileset.NOTHING;
    }

    public boolean hasVerticalSpaceAround(int x, int y) {
        return world[x][y] == Tileset.NOTHING &&
                world[x][y-1] == Tileset.NOTHING &&
                world[x][y+1] == Tileset.NOTHING;
    }

    public boolean hasSpaceAround(int x, int y) {
        return hasHorizontalSpaceAround(x, y) &&
                hasVerticalSpaceAround(x, y) &&
                world[x-1][y-1] == Tileset.NOTHING &&
                world[x-1][y+1] == Tileset.NOTHING &&
                world[x+1][y-1] == Tileset.NOTHING &&
                world[x+1][y+1] == Tileset.NOTHING;
    }

    public boolean isNoSpaceLeft() {
        for (int i = 1; i < height - 1; ++i) {
            for (int j = 1; j < width - 1; ++j) {
                if (hasSpaceAround(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private int findLeftBar(int x, int y) {
        for (int j = y - 2; j >= 0; --j) {
            if (world[x][j] != Tileset.NOTHING || !hasVerticalSpaceAround(x, j)) {
                return j;
            }
        }
        return -1;
    }

    private int findRightBar(int x, int y) {
        for (int j = y + 2; j < width; ++j) {
            if (world[x][j] != Tileset.NOTHING || !hasVerticalSpaceAround(x, j)) {
                return j;
            }
        }
        return width;
    }

    private int findTopBar(int x, int y) {
        for (int i = x - 2; i >= 0; --i) {
            if (world[i][y] != Tileset.NOTHING || !hasHorizontalSpaceAround(i, y)) {
                return i;
            }
        }
        return -1;
    }

    private int findBottomBar(int x, int y) {
        for (int i = x + 2; i < height; ++i) {
            if (world[i][y] != Tileset.NOTHING || !hasHorizontalSpaceAround(i, y)) {
                return i;
            }
        }
        return -1;
    }

    public ElementGenerator.Room generateMergeableRoom() {
        if (isNoSpaceLeft()) {
            throw new RuntimeException("No enough space for a new room");
        }
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(1, height - 1);
            y = random.nextInt(1, width - 1);
        } while (!hasSpaceAround(x, y));

        /* Generate a box like this:
         *         x1 ---- .............
         *                 .           .
         *                 .           .
         *         x2 ---- .............
         *                 |           |
         *  |-->           |           |
         *  v              y1          y2
         */

        int upperBound = findTopBar(x, y);
        int lowerBound = findBottomBar(x, y);

        final int x1 = random.nextInt(upperBound + 1, x);
        final int x2 = random.nextInt(x + 1, lowerBound);

        int rightmostLeftBarDistance = width + 1, leftmostRightBarDistance = width + 1, y1 = y, y2 = y;
        for (int i = x1; i <= x2; ++i) {
            if (x - findLeftBar(x, i) < rightmostLeftBarDistance) {
                y1 = findLeftBar(x, i);
            }
            if (findRightBar(x, i) - x < leftmostRightBarDistance) {
                y2 = findRightBar(x, i);
            }
        }
        return new ElementGenerator.Room(x2 - x1 + 1, y2 - y1 + 1, x1, y1);
    }
}
