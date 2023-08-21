package byog.util;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public final class ElementGenerator {
    public static final class Room extends ElementBase<TETile> {
        public Room(int height, int width, int positionX, int positionY) {
            super(TETile.class, height, width, positionX, positionY);
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
        public Hallway(int height, int width, int positionX, int positionY) {
            super(TETile.class, height, width, positionX, positionY);
            assert height == 3 || width == 3;

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

    public static final class HallwayWithATurn extends Hallway {
        public enum Shapes {TopLeft, TopRight, BottomLeft, BottomRight}

        public HallwayWithATurn(int height, int width, int positionX, int positionY, Shapes shape) {
            super(height, width, positionX, positionY);
            for (int i = 0; i < height; ++i) {
                for (int j = 0; j < width; ++j) {
                    data[i][j] = Tileset.NOTHING;
                }
            }
            switch (shape) {
                case TopLeft -> {
                    addTop();
                    addLeft();
                    data[1][2] = Tileset.FLOOR;
                    data[2][1] = Tileset.FLOOR;
                }
                case TopRight -> {
                    addTop();
                    addRight();
                    data[1][width - 3] = Tileset.FLOOR;
                    data[2][width - 2] = Tileset.FLOOR;
                }
                case BottomLeft -> {
                    addBottom();
                    addLeft();
                    data[height - 3][1] = Tileset.FLOOR;
                    data[height - 2][2] = Tileset.FLOOR;
                }
                case BottomRight -> {
                    addBottom();
                    addRight();
                    data[height - 2][width - 3] = Tileset.FLOOR;
                    data[height - 3][width - 2] = Tileset.FLOOR;
                }
            }
        }

        private void addTop() {
            for (int j = 0; j < width; ++j) {
                data[0][j] = Tileset.WALL;
                data[1][j] = Tileset.FLOOR;
                data[2][j] = Tileset.WALL;
            }
        }

        private void addLeft() {
            for (int i = 0; i < height; ++i) {
                data[i][0] = Tileset.WALL;
                data[i][1] = Tileset.FLOOR;
                data[i][2] = Tileset.WALL;
            }
        }

        private void addRight() {
            for (int i = 0; i < height; ++i) {
                data[i][width - 1] = Tileset.WALL;
                data[i][width - 2] = Tileset.FLOOR;
                data[i][width - 3] = Tileset.WALL;
            }
        }

        private void addBottom() {
            for (int j = 0; j < width; ++j) {
                data[height - 1][j] = Tileset.WALL;
                data[height - 2][j] = Tileset.FLOOR;
                data[height - 3][j] = Tileset.WALL;
            }
        }
    }

    public static final class Door extends ElementBase<TETile> {
        private boolean locked = true;
        public Door(int positionX, int positionY) {
            super(TETile.class, 1, 1, positionX, positionY);
            data[0][0] = Tileset.LOCKED_DOOR;
        }

        public void unlock() {
            assert locked;
            data[0][0] = Tileset.UNLOCKED_DOOR;
            locked = false;
        }
    }
}
