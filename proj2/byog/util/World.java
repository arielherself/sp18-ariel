package byog.util;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

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
}
