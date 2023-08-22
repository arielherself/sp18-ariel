package byog.util;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.lang.reflect.Array;
import java.util.*;

public class World2 extends MirrorCompatible<TETile> {
    public enum Orientations {Left, Right, Up, Down}
    public static final class RoomNotExpandableException extends RuntimeException {}
    public static class ExpansionPair {
        public final Coordinate coordinate;
        public final Orientations orientation;

        public ExpansionPair(Coordinate c, Orientations o) {
            coordinate = c;
            orientation = o;
        }
    }
    public static class ExpansionResult {
        public final LinkedList<ElementGenerator.Hallway> hallways = new LinkedList<>();
        public final ElementGenerator.Room room;

        public ExpansionResult(ElementGenerator.Room room, ElementGenerator.Hallway... hallways) {
            this.room = room;
            this.hallways.addAll(Arrays.asList(hallways));
        }
    }
    public static class Coordinate {
        public int x, y;

        public Coordinate(int x, int y) {
            setX(x);
            setY(y);
        }

        public int setX(int x) {
            this.x = x;
            return x;
        }

        public int setY(int y) {
            this.y = y;
            return y;
        }

        public static Coordinate copyOf(Coordinate c) {
            return new Coordinate(c.x, c.y);
        }
    }

    private final TETile[][] world;
    public TETile[][] getData() {
        return world;
    }
    private final int height, width;
    private final LinkedList<ElementGenerator.Room> rooms = new LinkedList<>();
    private final Random random = new Random();

    public World2(int height, int width) {
        this.height = height;
        this.width = width;
        world = (TETile[][]) Array.newInstance(TETile.class, height, width);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    protected boolean hasVerticalSpaceAround(Coordinate c) {
        final int x = c.x;
        final int y = c.y;
        return !(x == 0 || x == height - 1) &&
                world[x][y] == Tileset.NOTHING &&
                world[x-1][y] == Tileset.NOTHING &&
                world[x+1][y] == Tileset.NOTHING;
    }

    protected boolean hasHorizontalSpaceAround(Coordinate c) {
        final int x = c.x;
        final int y = c.y;
        return !(y == 0 || y == width - 1) &&
                world[x][y] == Tileset.NOTHING &&
                world[x][y-1] == Tileset.NOTHING &&
                world[x][y+1] == Tileset.NOTHING;
    }

    protected boolean hasSpaceAround(Coordinate c) {
        final int x = c.x;
        final int y = c.y;
        return hasHorizontalSpaceAround(c) &&
                hasVerticalSpaceAround(c) &&
                world[x-1][y-1] == Tileset.NOTHING &&
                world[x-1][y+1] == Tileset.NOTHING &&
                world[x+1][y-1] == Tileset.NOTHING &&
                world[x+1][y+1] == Tileset.NOTHING;
    }

    protected boolean expandable(Coordinate c, Orientations o) {
        int x = c.x, y = c.y;
        x = switch (o) {
            case Up -> x - 2;
            case Down -> x + 2;
            default -> x;
        };
        y = switch (o) {
            case Left -> y - 2;
            case Right -> y + 2;
            default -> x;
        };

        int x1 = c.x, y1 = c.y;
        x1 = switch (o) {
            case Up -> x - 1;
            case Down -> x + 1;
            default -> x;
        };
        y1 = switch (o) {
            case Left -> y - 1;
            case Right -> y + 1;
            default -> y;
        };
        return hasSpaceAround(new Coordinate(x, y)) && hasSpaceAround(new Coordinate(x1, y1));
    }

    protected int maxExpandLength(Coordinate c, Orientations o) {
        int s = 0;
        Coordinate nc = Coordinate.copyOf(c);
        while (expandable(nc, o)) {
            nc.setX(switch (o) {
                case Up -> nc.x - 1;
                case Right -> nc.x + 1;
                default -> nc.x;
            });
            nc.setY(switch (o) {
                case Left -> nc.y - 1;
                case Right -> nc.y + 1;
                default -> nc.y;
            });
            ++s;
        }
        return s;
    }

    protected int findLeftBar(Coordinate c) {
        final int x = c.x;
        final int y = c.y;
        for (int j = y - 2; j >= 0; --j) {
            if (world[x][j] != Tileset.NOTHING || !hasVerticalSpaceAround(new Coordinate(x, j))) {
                return j;
            }
        }
        return -1;
    }

    protected int findRightBar(Coordinate c) {
        final int x = c.x;
        final int y = c.y;
        for (int j = y + 2; j < width; ++j) {
            if (world[x][j] != Tileset.NOTHING || !hasVerticalSpaceAround(new Coordinate(x, j))) {
                return j;
            }
        }
        return width;
    }

    protected int findTopBar(Coordinate c) {
        final int x = c.x;
        final int y = c.y;
        for (int i = x - 2; i >= 0; --i) {
            if (world[i][y] != Tileset.NOTHING || !hasHorizontalSpaceAround(new Coordinate(i, y))) {
                return i;
            }
        }
        return -1;
    }

    protected int findBottomBar(Coordinate c) {
        final int x = c.x;
        final int y = c.y;
        for (int i = x + 2; i < height; ++i) {
            if (world[i][y] != Tileset.NOTHING || !hasHorizontalSpaceAround(new Coordinate(i, y))) {
                return i;
            }
        }
        return height;
    }

    public ExpansionResult expand(Coordinate c, Orientations o, int maximumLength, boolean noTurn) {
        // TODO: add areaLimit param
        final int expandLength = random.nextInt(1, Math.min(maximumLength, maxExpandLength(c, o)));
        final Coordinate nc = switch (o) {
            case Left -> new Coordinate(c.x, c.y - expandLength);
            case Right -> new Coordinate(c.x, c.y + expandLength);
            case Up -> new Coordinate(c.x - expandLength, c.y);
            case Down -> new Coordinate(c.x + expandLength, c.y);
        };
        final var hallway = switch (o) {
            case Left -> new ElementGenerator.Hallway(3, expandLength + 1, c.x - 1, c.y - expandLength - 1);
            case Right -> new ElementGenerator.Hallway(3, expandLength + 1, c.x - 1, c.y);
            case Up -> new ElementGenerator.Hallway(expandLength + 1, 3, c.x - expandLength - 1, c.y - 1);
            case Down -> new ElementGenerator.Hallway(expandLength + 1, 3, c.x, c.y - 1);
        };

        final boolean canTurn = switch (o) {
            case Up, Down -> expandable(nc, Orientations.Left) || expandable(nc, Orientations.Right);
            case Left, Right -> expandable(nc, Orientations.Up) || expandable(nc, Orientations.Down);
        };
        if (!noTurn && canTurn) {
            final boolean turn = random.nextBoolean();
            if (turn) {

                Orientations nextO;
                do {
                    nextO = random.nextInt(2) == 0 ? switch (o) {
                        case Up, Down -> Orientations.Left;
                        case Left, Right -> Orientations.Up;
                    } : switch (o) {
                        case Up, Down -> Orientations.Right;
                        case Left, Right -> Orientations.Down;
                    };
                } while (!expandable(nc, nextO));

                var turnResult = expand(nc, nextO, maximumLength, true);
                return new ExpansionResult(turnResult.room, turnResult.hallways.getFirst(), hallway);
            }
        }
        final ElementGenerator.Room room = switch (o) {
            case Left, Right -> {
                final int y1 = switch (o) {
                    case Left -> findLeftBar(nc); // excluded
                    case Right -> nc.y;
                    default -> -1;
                };
                final int y2 = switch (o) {
                    case Left -> nc.y; // excluded
                    case Right -> findRightBar(nc);
                    default -> -1;
                };
                final int yA = switch (o) {
                    case Left -> random.nextInt(y1 + 1, y2);
                    case Right -> nc.y;
                    default -> -1;
                };
                final int yB = switch (o) {
                    case Left -> nc.y;
                    case Right -> random.nextInt(y1 + 1, y2);
                    default -> -1;
                };
                int x1 = -1, x2 = -1;
                int lowestTopBarDistance = this.height, highestBottomBarDistance = this.height, temp;
                for (int y = yA; y <= yB; ++y) {
                    temp = findTopBar(new Coordinate(nc.x, y));
                    if (nc.x - temp < lowestTopBarDistance) {
                        lowestTopBarDistance = nc.x - temp;
                        x1 = temp;
                    }
                    temp = findBottomBar(new Coordinate(nc.x, y));
                    if (temp - nc.x < highestBottomBarDistance) {
                        highestBottomBarDistance = temp - nc.x;
                        x2 = temp;
                    }
                }
                final int xA = random.nextInt(x1 + 1, nc.x);
                final int xB = random.nextInt(nc.x + 1, x2);
                final int width = yB - yA + 1, height = xB - xA + 1;
                yield new ElementGenerator.Room(height, width, xA, yA);
            } case Up, Down -> {
                final int x1 = switch (o) {
                    case Up -> findTopBar(nc);
                    case Down -> nc.x;
                    default -> -1;
                };
                final int x2 = switch (o) {
                    case Up -> nc.x;
                    case Down -> findBottomBar(nc);
                    default -> -1;
                };
                final int xA = switch (o) {
                    case Up -> random.nextInt(x1 + 1, x2);
                    case Down -> nc.x;
                    default -> -1;
                };
                final int xB = switch (o) {
                    case Up -> nc.x;
                    case Down -> random.nextInt(x1 + 1, x2);
                    default -> -1;
                };
                int y1 = -1, y2 = -1;
                int rightmostLeftBarDistance = this.width, leftmostRightBarDistance = this.width, temp;
                for (int x = xA; x <= xB; ++x) {
                    temp = findLeftBar(new Coordinate(x, nc.y));
                    if (nc.y - temp < rightmostLeftBarDistance) {
                        rightmostLeftBarDistance = nc.y - temp;
                        y1 = temp;
                    }
                    temp = findRightBar(new Coordinate(x, nc.y));
                    if (temp - nc.y < leftmostRightBarDistance) {
                        leftmostRightBarDistance = temp - nc.y;
                    }
                }
                final int yA = random.nextInt(y1 + 1, nc.y);
                final int yB = random.nextInt(nc.y + 1, y2);
                final int width = yB - yA + 1, height = xB - xA + 1;
                yield new ElementGenerator.Room(height, width, xA, yA);
            }
        };

        return new ExpansionResult(room, hallway);
    }

    public ExpansionResult expand(ExpansionPair p, int maximumLength) {
        return expand(p.coordinate, p.orientation, maximumLength, false);
    }

    public ExpansionResult expand(ElementGenerator.Room r, int maximumLength)  throws RoomNotExpandableException {
        LinkedList<ExpansionPair> possibleExpansions = new LinkedList<>();
        for (int x = r.positionX; x < r.positionX + r.height; ++x) {
            Coordinate c;
            c = new Coordinate(x, r.positionY);
            if (expandable(c, Orientations.Left)) {
                possibleExpansions.add(new ExpansionPair(c, Orientations.Left));
            }
            c = new Coordinate(x, r.positionY + r.width + 1);
            if (expandable(c, Orientations.Right)) {
                possibleExpansions.add(new ExpansionPair(c, Orientations.Right));
            }
        }
        for (int y = r.positionY; y < r.positionY + r.width; ++y) {
            Coordinate c;
            c = new Coordinate(r.positionX, y);
            if (expandable(c, Orientations.Up)) {
                possibleExpansions.add(new ExpansionPair(c, Orientations.Up));
            }
            c = new Coordinate(r.positionX + r.height - 1, y);
            if (expandable(c, Orientations.Down)) {
                possibleExpansions.add(new ExpansionPair(c, Orientations.Down));
            }
        }

        if (possibleExpansions.isEmpty()) {
            throw new RoomNotExpandableException();
        }

        final int choice = random.nextInt(possibleExpansions.size());
        return expand(possibleExpansions.get(choice), maximumLength);
    }

    public ExpansionResult randomExpand(int maximumLength) {
        final int choice = random.nextInt(rooms.size());
        while (true) {
            try {
                return expand(rooms.get(choice), maximumLength);
            } catch (RoomNotExpandableException ignored) {}
        }
    }
}
