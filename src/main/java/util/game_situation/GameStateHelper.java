package util.game_situation;

import rlbot.cppinterop.RLBotDll;
import rlbot.gamestate.GameState;

public abstract class GameStateHelper {
    public static GameState getCurrentGameState() {
        return new rlbot.gamestate.GameState();
    }
    public static void applyGameState(final GameState gameState) {
        RLBotDll.setGameState(gameState.buildPacket());
    }
}
