package byog.Core.test;

import byog.Core.Game;

public class TestWorldGenerating {
    public static void main(String[] args) {
        Game game = new Game();
        game.initializeWorld(40, 50);
        game.render();
        System.out.println("finished");
    }
}
