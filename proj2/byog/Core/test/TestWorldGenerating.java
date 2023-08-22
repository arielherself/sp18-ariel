package byog.Core.test;

import byog.Core.Game;

public class TestWorldGenerating {
    public static void main(String[] args) {
        Game game = new Game();
        game.initializeWorld(20, 20);
        game.render();
    }
}
