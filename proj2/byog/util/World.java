package byog.util;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.lang.reflect.Array;
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
        world = (TETile[][]) Array.newInstance(TETile.class, height, width);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    public ElementGenerator.Room[] getRooms() {
        return rooms.toArray(new ElementGenerator.Room[rooms.size()]);
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

    protected boolean hasVerticalSpaceAround(int x, int y) {
        return !(x == 0 || x == height - 1) &&
                world[x][y] == Tileset.NOTHING &&
                world[x-1][y] == Tileset.NOTHING &&
                world[x+1][y] == Tileset.NOTHING;
    }

    protected boolean hasHorizontalSpaceAround(int x, int y) {
        return !(y == 0 || y == width - 1) &&
                world[x][y] == Tileset.NOTHING &&
                world[x][y-1] == Tileset.NOTHING &&
                world[x][y+1] == Tileset.NOTHING;
    }

    protected boolean hasSpaceAround(int x, int y) {
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

    protected int findLeftBar(int x, int y) {
        for (int j = y - 2; j >= 0; --j) {
            if (world[x][j] != Tileset.NOTHING || !hasVerticalSpaceAround(x, j)) {
                return j;
            }
        }
        return -1;
    }

    protected int findRightBar(int x, int y) {
        for (int j = y + 2; j < width; ++j) {
            if (world[x][j] != Tileset.NOTHING || !hasVerticalSpaceAround(x, j)) {
                return j;
            }
        }
        return width;
    }

    protected int findTopBar(int x, int y) {
        for (int i = x - 2; i >= 0; --i) {
            if (world[i][y] != Tileset.NOTHING || !hasHorizontalSpaceAround(i, y)) {
                return i;
            }
        }
        return -1;
    }

    protected int findBottomBar(int x, int y) {
        for (int i = x + 2; i < height; ++i) {
            if (world[i][y] != Tileset.NOTHING || !hasHorizontalSpaceAround(i, y)) {
                return i;
            }
        }
        return height;
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

        int upperBound = findTopBar(x, y); // excluded
        int lowerBound = findBottomBar(x, y); // excluded

        final int x1 = (upperBound + 1 < x) ? random.nextInt(upperBound + 1, x) : x;
        final int x2 = (x + 1 < lowerBound - 1) ? random.nextInt(x + 1, lowerBound) : lowerBound - 1;

        int rightmostLeftBarDistance = width + 1, leftmostRightBarDistance = width + 1, y1 = y, y2 = y;
        for (int i = x1; i <= x2; ++i) {
            if (y - findLeftBar(i, y) < rightmostLeftBarDistance) {
                y1 = findLeftBar(i, y);
                rightmostLeftBarDistance = y - y1;
            }
            if (findRightBar(i, y) - y < leftmostRightBarDistance) {
                y2 = findRightBar(i, y);
                leftmostRightBarDistance = y2 - y;
            }
        }
        y1 = (y1 + 1 < y) ? random.nextInt(y1 + 1, y) : y - 1;
        y2 = (y + 1 < y2) ? random.nextInt(y + 1, y2) : y + 1;
        return new ElementGenerator.Room(x2 - x1 + 1, y2 - y1 + 1, x1, y1);
    }


    public ElementGenerator.Door buildLockedDoor() {
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

    protected ElementGenerator.Hallway buildHorizontalHallway(ElementGenerator.Room roomA, ElementGenerator.Room roomB)
            throws RuntimeException {
        if (roomA.positionX >= roomB.positionX + roomB.height || roomB.positionX >= roomA.positionX + roomA.height) {
            throw new RuntimeException();
        }

        // Keep the roomA at the left of the roomB.
        if (roomA.positionY > roomB.positionY) {
            ElementGenerator.Room temp = roomA;
            roomA = roomB;
            roomB = temp;
        }

        int start = -1, end = -1;
        for (int i = roomA.positionX + 1; i < roomA.positionX + roomA.height - 1; ++i) {
            if ((i >= (roomB.positionX + 1)) && (i < (roomB.positionX + roomB.height - 1))) {
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
        if (start != -1 && end == -1) {
            end = roomA.positionX + roomA.height - 1;
        }

        LinkedList<ElementGenerator.Hallway> candidates = new LinkedList<>();
        for (int x = start; x < end; ++x) {
            boolean connectable = true;
            for (int i = roomA.positionY + roomA.width; i < roomB.positionY; ++i) {
                if (!hasVerticalSpaceAround(x, i)) {
                    connectable = false;
                    break;
                }
            }
            if (connectable) {
                candidates.add(new ElementGenerator.Hallway(3, roomB.positionY - roomA.positionY - roomA.width + 2,
                        x - 1, roomA.positionY + roomA.width - 1));
            }
        }
        if (candidates.isEmpty()) {
            throw new RuntimeException("Unable to build a horizontalHallway");
        }

        Random random = new Random();
        return candidates.get(random.nextInt(candidates.size()));
    }

    protected ElementGenerator.Hallway buildVerticalHallway(ElementGenerator.Room roomA, ElementGenerator.Room roomB)
            throws RuntimeException {
        if (roomA.positionY >= roomB.positionY + roomB.width || roomB.positionY >= roomA.positionY + roomA.width) {
            throw new RuntimeException();
        }

        // Keep the roomA at the top of the roomB.
        if (roomA.positionX > roomB.positionX) {
            ElementGenerator.Room temp = roomA;
            roomA = roomB;
            roomB = temp;
        }

        int start = -1, end = -1;
        for (int i = roomA.positionY + 1; i < roomA.positionY + roomA.width - 1; ++i) {
            if (i >= roomB.positionY + 1 && i < roomB.positionY + roomB.width - 1) {
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
        if (start != -1 && end == -1) {
            end = roomA.positionY + roomA.width - 1;
        }

        LinkedList<ElementGenerator.Hallway> candidates = new LinkedList<>();
        for (int y = start; y < end; ++y) {
            boolean connectable = true;
            for (int j = roomA.positionX + roomA.height; j < roomB.positionX; ++j) {
                if (!hasHorizontalSpaceAround(j, y)) {
                    connectable = false;
                    break;
                }
            }
            if (connectable) {
                candidates.add(new ElementGenerator.Hallway(roomB.positionX - roomA.positionX -roomA.height + 2, 3,
                        roomA.positionX + roomA.height - 1, y - 1));
            }
        }
        if (candidates.isEmpty()) {
            throw new RuntimeException("Unable to build a verticalHallway");
        } else {
            Random random = new Random();
            return candidates.get(random.nextInt(candidates.size()));
        }
    }

    protected LinkedList<ElementGenerator.Hallway> buildHallwayDirectly(ElementGenerator.Room roomA, ElementGenerator.Room roomB) {
        LinkedList<ElementGenerator.Hallway> result = new LinkedList<>();
        try {
            result.add(buildHorizontalHallway(roomA, roomB));
        } catch (RuntimeException ignored) {}
        try {
            result.add(buildVerticalHallway(roomA, roomB));
        } catch (RuntimeException ignored) {}

        return result;
    }

    protected LinkedList<ElementGenerator.Hallway> buildHallwaysWithADownwardTurn(ElementGenerator.Room roomA, ElementGenerator.Room roomB) {
        /*
         *                 ...
         *             |  roomA  |
         *             |---------|
         *       x1 ______ | _______ |------
         *        x _______|________ | roomB ...
         *                 |       | |------
         *                 y       y1 > y
         */

        LinkedList<ElementGenerator.Hallway> result = new LinkedList<>();

        if (roomA.positionY > roomB.positionY) {
            var temp = roomA;
            roomA = roomB;
            roomB = temp;
        }

        if (roomA.positionX + roomA.height >= roomB.positionX + roomB.height) {
            return result;
        }

        assert roomB.positionY > roomA.positionY + 2;
        final int x1 = Math.max(roomA.positionX + roomA.height, roomB.positionX + 1);
        final int x2 = roomB.positionX + roomB.height - 2;
        final int y1 = roomA.positionY; // excluded
        final int y2 = Math.min(roomA.positionY + roomA.width, roomB.positionY); // excluded

        for (int x = x1; x <= x2; ++x) {
            seekForCrossingPoint: for (int y = y1 + 1; y < y2; ++y) {
                int yA = y - 1, yB = roomB.positionY; // included (outer bound)
                ElementGenerator.HallwayWithATurn.Shapes shape;

                if (!hasSpaceAround(x, y)) {
                    continue;
                }

                for (int i = roomA.positionX + roomA.height; i <= x; ++i) {
                    if (!hasHorizontalSpaceAround(i, y)) {
                        continue seekForCrossingPoint;
                    }
                }

                if (yB < yA) { // Keep yB on the right side of yA
                    shape = ElementGenerator.HallwayWithATurn.Shapes.BottomRight;
                    yB = y + 1;
                    yA = roomB.positionY + roomB.width - 1;

                    for (int j = yA + 1; j < yB; ++j) {
                        if (!hasVerticalSpaceAround(x, j)) {
                            continue seekForCrossingPoint;
                        }
                    }

                } else {
                    shape = ElementGenerator.HallwayWithATurn.Shapes.BottomLeft;

                    for (int j = yA + 1; j < yB; ++j) {
                        if (!hasVerticalSpaceAround(x, j)) {
                            continue seekForCrossingPoint;
                        }
                    }
                }


                result.add(new ElementGenerator.HallwayWithATurn(x - roomA.height - roomA.positionX + 2, yB - yA + 1,
                        roomA.positionX + roomA.height - 1, yA, shape));
            }
        }

        return result;
    }

    protected LinkedList<ElementGenerator.Hallway> buildHallwaysWithAnUpwardTurn(ElementGenerator.Room roomA, ElementGenerator.Room roomB) {
        /*
         *                 y
         *                 |         |------
         *        x _______|________ | roomB ...
         *   x1 > x ______ | ______  |------
         *             |---------|
         *             |  roomA  |
         *                 ...
         */

        LinkedList<ElementGenerator.Hallway> result = new LinkedList<>();

        if (roomA.positionY > roomB.positionY) {
            var temp = roomA;
            roomA = roomB;
            roomB = temp;
        }

        if (roomA.positionX <= roomB.positionX) {
            return result;
        }

        assert roomB.positionY > roomA.positionY + 2;
        final int x1 = Math.min(roomA.positionX - 1, roomB.positionX + roomB.height - 2);
        final int x2 = roomB.positionX + 1;
        final int y1 = roomA.positionY; // excluded
        final int y2 = Math.min(roomA.positionY + roomA.width - 1, roomB.positionY); // excluded

        for (int x = x2; x <= x1; ++x) {
            seekForCrossingPoint: for (int y = y1 + 1; y < y2; ++y) {
                int yA = y - 1, yB = roomB.positionY; // included (outer bound)
                ElementGenerator.HallwayWithATurn.Shapes shape;

                if (!hasSpaceAround(x, y)) {
                    continue;
                }

                for (int i = x; i < roomA.positionX; ++i) {
                    if (!hasHorizontalSpaceAround(i, y)) {
                        continue seekForCrossingPoint;
                    }
                }

                if (yB < yA) { // Keep yB on the right side of yA
                    shape = ElementGenerator.HallwayWithATurn.Shapes.TopRight;
                    yB = y + 1;
                    yA = roomB.positionY + roomB.width - 1;

                    for (int j = yA + 1; j < yB; ++j) {
                        if (!hasVerticalSpaceAround(x, j)) {
                            continue seekForCrossingPoint;
                        }
                    }

                } else {
                    shape = ElementGenerator.HallwayWithATurn.Shapes.TopLeft;

                    for (int j = yA + 1; j < yB; ++j) {
                        if (!hasVerticalSpaceAround(x, j)) {
                            continue seekForCrossingPoint;
                        }
                    }
                }


                result.add(new ElementGenerator.HallwayWithATurn(roomA.positionX - x + 2, yB - yA + 1,
                        x - 1, yA, shape));
            }
        }

        return result;
    }

    protected LinkedList<ElementGenerator.Hallway> buildHallwaysWithATurn(ElementGenerator.Room roomA, ElementGenerator.Room roomB) {
        LinkedList<ElementGenerator.Hallway> result = new LinkedList<>();
        if (roomA.positionX > roomB.positionX) {
            result.addAll(buildHallwaysWithADownwardTurn(roomB, roomA));
//            result.addAll(buildHallwaysWithAnUpwardTurn(roomA, roomB));
        } else if (roomA.positionX < roomB.positionX) {
            result.addAll(buildHallwaysWithADownwardTurn(roomA, roomB));
//            result.addAll(buildHallwaysWithAnUpwardTurn(roomB, roomA));
        }
        return result;
    }

    public ElementGenerator.Hallway buildHallway(ElementGenerator.Room roomA, ElementGenerator.Room roomB) {
        LinkedList<ElementGenerator.Hallway> candidates = new LinkedList<>();
        try {
            candidates.addAll(buildHallwayDirectly(roomA, roomB));
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
