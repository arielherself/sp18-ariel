package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.util.World;

import java.util.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int AREA = WIDTH * HEIGHT;

    private int numberOfRooms;
    private final World world = new World(HEIGHT, WIDTH);

    public Game() {
        ter.initialize(WIDTH, HEIGHT);
    }

    public void render() {
        ter.renderFrame(world.mirrored(TETile.class));
    }

    public void initializeWorld(int minNumberOfRooms, int maxNumberOfRooms) {
        Random random = new Random();

        world.cacheAndMerge(world.buildFirstRoom(50));
        World.ExpansionResult result;
        int expectedNumberOfRooms = random.nextInt(Math.max(1, minNumberOfRooms), maxNumberOfRooms + 1);
        for (int i = 1; i < expectedNumberOfRooms; ++i) {
            try {
                result = world.randomExpand(5, 50);
            } catch (World.RoomNotExpandableException ignored) {
                break;
            }
            world.cacheAndMerge(result.room);
            world.cacheAndMerge(result.hallway);
            System.out.println(TETile.toString(world.mirrored(TETile.class)));
        }
    }

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
