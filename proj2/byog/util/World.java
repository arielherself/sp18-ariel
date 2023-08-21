package byog.util;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.Random;

public class World extends MirrorCompatible<TETile> {
    private final TETile[][] world;
    private final LinkedList<ElementBase<TETile>> elements = new LinkedList<>();
    private final LinkedList<ElementGenerator.Room> rooms = new LinkedList<>();
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

    public ElementBase<TETile>[] getElements() {
        return (ElementBase<TETile>[]) elements.toArray();
    }

    public ElementGenerator.Room[] getRooms() {
        return (ElementGenerator.Room[]) rooms.toArray();
    }

    public void cacheAndMerge(ElementBase<TETile> element) {
        assert !elements.contains(element);
        elements.add(element);
        if (element instanceof ElementGenerator.Room roomElement) {
            rooms.add(roomElement);
        }
        for (int i = 0; i < element.height; ++i) {
           for (int j = 0; j < element.width; ++j) {
               if (element.data[i][j] != Tileset.NOTHING) {
                   world[element.positionX + i][element.positionY + j] = element.data[i][j];
               }
           }
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

    public boolean isNoSpaceLeftForRooms() {
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

    public ElementGenerator.Room buildMergeableRoom() throws RuntimeException {
        if (isNoSpaceLeftForRooms()) {
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
        y1 = random.nextInt(y1, y - 1);
        y2 = random.nextInt(y + 2, y2 + 1);
        return new ElementGenerator.Room(x2 - x1 + 1, y2 - y1 + 1, x1, y1);
    }


    public ElementGenerator.Door generateLockedDoor() {
        LinkedList<Integer> wallPositionX = new LinkedList<>();
        LinkedList<Integer> wallPositionY = new LinkedList<>();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (world[i][j] == Tileset.WALL) {
                    wallPositionX.addLast(i);
                    wallPositionY.addLast(j);
                }
            }
        }
        Random random = new Random();
        final int targetNo = random.nextInt(0, wallPositionX.size());
        return new ElementGenerator.Door(wallPositionX.get(targetNo), wallPositionY.get(targetNo));
    }

    public ElementGenerator.Hallway buildHorizontalHallway(ElementGenerator.Room roomA, ElementGenerator.Room roomB)
            throws RuntimeException {
        // Keep the roomA at the left of the roomB.
        if (roomA.positionY > roomB.positionY) {
            ElementGenerator.Room temp = roomA;
            roomA = roomB;
            roomB = temp;
        }

        int start = -1, end = -1;
        for (int i = roomA.positionX; i < roomA.positionX + roomA.height; ++i) {
            if (i >= roomB.positionX && i <= roomB.positionX + roomB.height) {
                if (start == -1) {
                    start = i;
                }
            } else {
                if (start != -1) {
                    end = i;
                    break;
                }
            }
        }

        boolean ableToBuild = false;
        for (int x = start; x < end; ++x) {
            boolean connectable = true;
            for (int i = roomA.positionY + roomA.width; i < roomB.positionY; ++i) {
                if (!hasVerticalSpaceAround(x, i)) {
                    connectable = false;
                    break;
                }
            }
            if (connectable) {
                ableToBuild = true;
                break;
            }
        }
        if (!ableToBuild) {
            throw new RuntimeException("Unable to build a horizontalHallway");
        }

        Random random = new Random();
        int x;
        ElementGenerator.Hallway result;
        boolean free;
        do {
            x = random.nextInt(start, end);
            free = true;
            for (int i = roomA.positionY + roomA.width; i < roomB.positionY; ++i) {
                if (!hasVerticalSpaceAround(x, i)) {
                    free = false;
                    break;
                }
            }
        } while (!free);

        // Finally build the hallway after checking there's no obstacles
        return new ElementGenerator.Hallway(3, roomB.positionY - roomA.positionY -roomA.width + 2,
                x, roomA.positionY + roomA.width - 1);
    }

    public ElementGenerator.Hallway buildVerticalHallway(ElementGenerator.Room roomA, ElementGenerator.Room roomB)
            throws RuntimeException {
        // Keep the roomA at the top of the roomB.
        if (roomA.positionX > roomB.positionX) {
            ElementGenerator.Room temp = roomA;
            roomA = roomB;
            roomB = temp;
        }

        int start = -1, end = -1;
        for (int i = roomA.positionY; i < roomA.positionY + roomA.width; ++i) {
            if (i >= roomB.positionY && i <= roomB.positionY + roomB.width) {
                if (start == -1) {
                    start = i;
                }
            } else {
                if (start != -1) {
                    end = i;
                    break;
                }
            }
        }

        boolean ableToBuild = false;
        for (int y = start; y < end; ++y) {
            boolean connectable = true;
            for (int j = roomA.positionX + roomA.height; j < roomB.positionX; ++j) {
                if (!hasVerticalSpaceAround(j, y)) {
                    connectable = false;
                    break;
                }
            }
            if (connectable) {
                ableToBuild = true;
                break;
            }
        }
        if (!ableToBuild) {
            throw new RuntimeException("Unable to build a horizontalHallway");
        }

        Random random = new Random();
        int y;
        ElementGenerator.Hallway result;
        boolean free;
        do {
            y = random.nextInt(start, end);
            free = true;
            for (int j = roomA.positionX + roomA.height; j < roomB.positionX; ++j) {
                if (!hasVerticalSpaceAround(j, y)) {
                    free = false;
                    break;
                }
            }
        } while (!free);

        // Finally build the hallway after checking there's no obstacles
        return new ElementGenerator.Hallway(roomB.positionX - roomA.positionX -roomA.height + 2, 3,
                roomA.positionX + roomA.height - 1, y);
    }

    public ElementGenerator.Hallway tryToBuildHallwayDirectly(ElementGenerator.Room roomA, ElementGenerator.Room roomB)
            throws RuntimeException {
        for (int i = roomA.positionX; i < roomA.positionX + roomA.height; ++i) {
            if (i >= roomB.positionX && i <= roomB.positionX + roomB.height) {
                try {
                    return buildHorizontalHallway(roomA, roomB);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        for (int j = roomA.positionY; j < roomA.positionY + roomA.width; ++j) {
            if (j >= roomB.positionY && j <= roomB.positionY + roomB.width) {
                try {
                    return buildVerticalHallway(roomA, roomB);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        // TODO refactor: do not throw an exception & -> buildHallwayDirectly
        throw new RuntimeException("Unable to build a hallway directly");
    }

    public LinkedList<ElementGenerator.Hallway> buildHallwaysWithADownwardTurn(ElementGenerator.Room roomA, ElementGenerator.Room roomB)
            throws RuntimeException {
        /*
         *                 ...
         *             |  roomA  |
         *             |---------|
         *       x1 ______ | _______ |------
         *        x _______|________ | roomB ...
         *                 |       | |------
         *                 y       y1 > y
         */

        if (roomA.positionY > roomB.positionY) {
            var temp = roomA;
            roomA = roomB;
            roomB = temp;
        }

        assert roomB.positionY > roomA.positionY + 2;
        final int x1 = Math.max(roomA.positionX + roomA.height, roomB.positionX); // excluded
        final int x2 = roomB.positionX + roomB.height;
        final int y1 = roomA.positionY;
        final int y2 = Math.min(roomA.positionY + roomA.width, roomB.positionY); // excluded

        LinkedList<ElementGenerator.Hallway> result = new LinkedList<>();
        for (int x = x1; x < x2; ++x) {
            seekForCrossingPoint: for (int y = y1; y < y2; ++y) {
                int y1_clone = y1, y_clone = y;
                ElementGenerator.HallwayWithATurn.Shapes shape;
                if (y1_clone < y_clone) { // Keep roomB on the right side of roomA
                    shape = ElementGenerator.HallwayWithATurn.Shapes.BottomLeft;
                    final var temp = y1_clone;
                    y1_clone = y_clone;
                    y_clone = temp;
                } else {
                    shape = ElementGenerator.HallwayWithATurn.Shapes.BottomRight;
                }
                for (int i = x1; i <= x; ++i) {
                    if (!hasVerticalSpaceAround(i, y)) {
                        continue seekForCrossingPoint;
                    }
                }
                for (int j = y_clone; j <= y1_clone; ++j) {
                    if (!hasHorizontalSpaceAround(x, y1)) {
                        continue seekForCrossingPoint;
                    }
                }
                if (!hasSpaceAround(x, y)) {
                    continue;
                }
                result.add(new ElementGenerator.HallwayWithATurn(x - roomA.positionX - roomA.height + 2, y1_clone - y_clone + 1,
                        roomA.positionX + roomA.height - 1, y1_clone, shape));
            }
        }

        return result;
    }

    public LinkedList<ElementGenerator.Hallway> buildHallwaysWithAnUpwardTurn(ElementGenerator.Room roomA, ElementGenerator.Room roomB)
            throws RuntimeException {
        /*
         *                 y
         *                 |         |------
         *        x _______|________ | roomB ...
         *   x1 > x ______ | ______  |------
         *             |---------|
         *             |  roomA  |
         *                 ...
         */

        if (roomA.positionX > roomB.positionX) {
            var temp = roomA;
            roomA = roomB;
            roomB = temp;
        }

        assert roomB.positionX > roomA.positionX + 2;
        final int x1 = Math.min(roomA.positionX, roomB.positionX + roomB.height) - 1;
        final int x2 = roomB.positionX; // x2 < x1
        final int y1 = roomA.positionY;
        final int y2 = Math.min(roomA.positionY + roomA.width, roomB.positionY); // excluded

        LinkedList<ElementGenerator.Hallway> result = new LinkedList<>();
        for (int x = x1; x < x2; ++x) {
            seekForCrossingPoint: for (int y = y1; y < y2; ++y) {
                int y1_clone = y1, y_clone = y;
                ElementGenerator.HallwayWithATurn.Shapes shape;
                if (y1_clone < y_clone) { // Keep roomB on the right side of roomA
                    shape = ElementGenerator.HallwayWithATurn.Shapes.TopLeft;
                    final var temp = y1_clone;
                    y1_clone = y_clone;
                    y_clone = temp;
                } else {
                    shape = ElementGenerator.HallwayWithATurn.Shapes.TopRight;
                }
                for (int i = x; i <= x1; ++i) {
                    if (!hasVerticalSpaceAround(i, y)) {
                        continue seekForCrossingPoint;
                    }
                }
                for (int j = y_clone; j <= y1_clone; ++j) {
                    if (!hasHorizontalSpaceAround(x, y1)) {
                        continue seekForCrossingPoint;
                    }
                }
                if (!hasSpaceAround(x, y)) {
                    continue;
                }
                result.add(new ElementGenerator.HallwayWithATurn(roomA.positionX - x, y1_clone - y_clone + 1,
                        x, y1_clone, shape));
            }
        }

        return result;
    }

    public LinkedList<ElementGenerator.Hallway> buildHallwaysWithATurn(ElementGenerator.Room roomA, ElementGenerator.Room roomB) {
        LinkedList<ElementGenerator.Hallway> result = new LinkedList<>();
        try {
            result.addAll(buildHallwaysWithAnUpwardTurn(roomA, roomB));
        } catch (RuntimeException ignored) {}
        try {
            result.addAll(buildHallwaysWithADownwardTurn(roomA, roomB));
        } catch (RuntimeException ignored) {}
        return result;
    }

    public ElementGenerator.Hallway buildHallway(ElementGenerator.Room roomA, ElementGenerator.Room roomB) {
        LinkedList<ElementGenerator.Hallway> candidates = new LinkedList<>();
        try {
            candidates.add(tryToBuildHallwayDirectly(roomA, roomB));
        } catch (RuntimeException ignored) {}

        candidates.addAll(buildHallwaysWithATurn(roomA, roomB));

        if (candidates.isEmpty()) {
            return null;
        } else {
            Random random = new Random();
            return candidates.get(random.nextInt(0, candidates.size()));
        }
    }
}
