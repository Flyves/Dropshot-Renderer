package rlbotexample.app.physics.state_setter;

import rlbot.flat.GameTickPacket;
import rlbot.gamestate.*;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.discrete_functions.ExponentialSmoother3D;
import util.game_situation.GameStateHelper;
import util.math.vector.Ray3;
import util.math.vector.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BallStateSetter {
    // for camera stuff, we can try to use bakkes mod commands in game with the robot java library to setup everything on game startup
    // in the case that the game crashes and we need to restart it
    //
    // we need to set the spectator mode to free cam:
    // bakkesmod camera setting command to set it high up in the air: bind KEY "SpectateSetCamera 0 0 4330 -90 90 0 120.000"

    public static final Stack<Integer> tileIndexesToBreak = new Stack<>();
    private static int stupidCycleCounter = 0;
    private static boolean isBackgroundPositionEnabled = true;

    public static void handleBallState(DataPacket input) {
        stupidCycleCounter++;
        if(stupidCycleCounter == 2) {
            stupidCycleCounter = 0;
        }
        else {
            return;
        }

        // if we didn't find any tile, don't do anything for now
        // this likely indicates that we're done breaking dropshot tiles for the current frame
        if(tileIndexesToBreak.isEmpty()) {
            if(isBackgroundPositionEnabled) {
                BallStateSetter.setBallPositionAndSpeed(new Vector3(0, 0, 8000), new Vector3(0, 0, 0));
            }
            return;
        }

        int tileIndexToBreak = tileIndexesToBreak.pop();

        BallStateSetter.setBallPositionAndSpeed(input.dropshotTilePositions.get(tileIndexToBreak).plus(new Vector3(0, 0, 90)), new Vector3(0, 0, -1000));
    }

    public static void setIsBackgroundPositionEnabled(final boolean isBackgroundPositionEnabled) {
        BallStateSetter.isBackgroundPositionEnabled = isBackgroundPositionEnabled;
    }

    private static void setBallPositionAndSpeed(final Vector3 position, final Vector3 velocity) {
        GameState gameState = GameStateHelper.getCurrentGameState();
        gameState.withBallState(new BallState(new PhysicsState()
                .withLocation(position.scaled(1, 1, 1).toDesiredVector3())
                .withAngularVelocity(new DesiredVector3(0f, 0f, 0f))
                .withVelocity(velocity.toDesiredVector3())));
        GameStateHelper.applyGameState(gameState);
    }
}