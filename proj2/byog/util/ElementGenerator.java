package byog.util;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public final class ElementGenerator {
    public static final class Room extends ElementBase<TETile> {
        public Room(int height, int width, World.Coordinate c) {
            super(TETile.class, height, width, c.x, c.y);
            assert height >= 3 && width >= 3;

            // Build walls
            for (int j = 0; j < width; ++j) {
                data[0][j] = Tileset.WALL;
                data[height - 1][j] = Tileset.WALL;
            }
            for (int i = 0; i < height; ++i) {
                data[i][0] = Tileset.WALL;
                data[i][width - 1] = Tileset.WALL;
            }

            //Build the room
            for (int i = 1; i < height - 1; ++i) {
                for (int j = 1; j < width - 1; ++j) {
                    data[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    public static class Hallway extends ElementBase<TETile> {
        public Hallway(int height, int width, World.Coordinate c) {
            super(TETile.class, height, width, c.x, c.y);
            assert (height == 3 || width == 3) && height != width;

            if (height == 3) {
                for (int j = 0; j < width; ++j) {
                    data[0][j] = Tileset.WALL;
                    data[1][j] = Tileset.FLOOR;
                    data[2][j] = Tileset.WALL;
                }
            } else { // width == 3
                for (int i = 0; i < height; ++i) {
                    data[i][0] = Tileset.WALL;
                    data[i][1] = Tileset.FLOOR;
                    data[i][2] = Tileset.WALL;
                }
            }
        }
    }

    public static class HorizontalHallway extends Hallway {
        public HorizontalHallway(int height, int width, World.Coordinate c) {
            super(height, width, c);
            assert height == 3;
            for (int j = 0; j < width; ++j) {
                data[0][j] = Tileset.WALL;
                data[1][j] = Tileset.FLOOR;
                data[2][j] = Tileset.WALL;
            }
        }
    }

    public static class VerticalHallway extends Hallway {
        public VerticalHallway(int height, int width, World.Coordinate c) {
            super(height, width, c);
            assert width == 3;
            for (int i = 0; i < height; ++i) {
                data[i][0] = Tileset.WALL;
                data[i][1] = Tileset.FLOOR;
                data[i][2] = Tileset.WALL;
            }
        }
    }

    public static final class Door extends ElementBase<TETile> {
        private boolean locked = true;
        public Door(World.Coordinate c) {
            super(TETile.class, 1, 1, c.x, c.y);
            data[0][0] = Tileset.LOCKED_DOOR;
        }

        public void unlock() {
            assert locked;
            data[0][0] = Tileset.UNLOCKED_DOOR;
            locked = false;
        }
    }
}
