package rlbotexample.app.physics.game;

import rlbotexample.app.physics.game.states.car_position.CarPositionSetter;
import rlbotexample.app.physics.game.states.tile_display.GetNextFrameData;
import rlbotexample.app.physics.game.states.tile_display.SetTiles;
import rlbotexample.app.physics.game.states.tile_display.WaitForUIElementsToDisappearOnGameStart;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.StateMachine;

import java.awt.*;

public class CurrentGame {


    private static final StateMachine TileMachine = new StateMachine(new WaitForUIElementsToDisappearOnGameStart());
    private static final StateMachine CarPositionMachine = new StateMachine(new CarPositionSetter());

    public static void step(DataPacket input) {
        CarPositionMachine.exec(input);
        TileMachine.exec(input);
    }
}
