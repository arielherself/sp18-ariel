package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.util.ElementGenerator;
import byog.util.World;
import byog.util.World2;

import java.util.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int AREA = WIDTH * HEIGHT;

    private int numberOfRooms;
    private World2 world = new World2(HEIGHT, WIDTH);

    public Game() {
        ter.initialize(WIDTH, HEIGHT);
    }

    public void render() {
        ter.renderFrame(world.mirrored(TETile.class));
    }

    public void initializeWorld2(int minNumberOfRooms, int maxNumberOfRooms) {
        Random random = new Random();

        world.cacheAndMerge(world.buildFirstRoom(50));
        World2.ExpansionResult result;
        int expectedNumberOfRooms = random.nextInt(Math.max(1, minNumberOfRooms), maxNumberOfRooms + 1);
        for (int i = 1; i < expectedNumberOfRooms; ++i) {
            var result = world.randomExpand(5);
                result = world.randomExpand(5, 50);
            world.cacheAndMerge(result.room);
            world.cacheAndMerge(result.hallway);
        }
    }

//    public void initializeWorld(int maxNumberOfRooms, int maxNumberOfHallways) {
//        Random random = new Random();
//
//        /* Place the rooms */
//        int expectedNumberOfRooms = random.nextInt(1, maxNumberOfRooms + 1);
//        numberOfRooms = 0;
//        while (numberOfRooms < expectedNumberOfRooms && !world.isNoSpaceLeftForRooms()) {
//            world.cacheAndMerge(world.buildMergeableRoom(AREA / expectedNumberOfRooms));
//            ++numberOfRooms;
//        }
//
//        /* Place the hallways */
//        Map<ElementGenerator.Room, Integer> roomConnections = new HashMap<>();
//        int expectedNumberOfHallways = random.nextInt(0, maxNumberOfHallways + 1);
//        final var rooms = world.getRooms();
//        for (int i = 0; i < rooms.length; ++i) {
//            roomConnections.put(rooms[i], 0);
//        }
//        for (int i = 0; i < expectedNumberOfHallways; ++i) {
//            var roomA = rooms[random.nextInt(0, rooms.length)];
//            var roomB = rooms[random.nextInt(0, rooms.length)];
//            while (roomA == roomB) {
//                roomB = rooms[random.nextInt(0, rooms.length)];
//            }
//            var hallway = world.buildHallway(roomA, roomB);
//            if (hallway != null) {
//                try {
//                    world.cacheAndMerge(hallway);
//                    roomConnections.put(roomA, roomConnections.get(roomA) + 1);
//                    roomConnections.put(roomB, roomConnections.get(roomB) + 1);
//                } catch (AssertionError ignored) {}
//            }
//        }
//        System.out.println(roomConnections);
//        for (var roomA : roomConnections.keySet()) { // All rooms must be connected twice!
//            if (roomConnections.get(roomA) < 2) {
//                while (true) {
//                    var roomB = rooms[random.nextInt(0, rooms.length)];
//                    while (roomA == roomB) {
//                        roomB = rooms[random.nextInt(0, rooms.length)];
//                    }
//                    var hallway = world.buildHallway(roomA, roomB);
//                    if (hallway == null) {
//                        continue;
//                    }
//                    try {
//                        world.cacheAndMerge(hallway);
//                        roomConnections.put(roomA, roomConnections.get(roomA) + 1);
//                        break;
//                    } catch (AssertionError ignored) {}
//                }
//            }
//        }
//
//        /* Place the door */
//        world.buildLockedDoor();
//    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame = null;
        // TODO add: game process
        finalWorldFrame = world.mirrored(TETile.class);
        return finalWorldFrame;
    }
}
