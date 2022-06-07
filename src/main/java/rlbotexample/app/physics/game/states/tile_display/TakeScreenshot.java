package rlbotexample.app.physics.game.states.tile_display;

import rlbotexample.app.physics.game.states.FrameData;
import rlbotexample.dynamic_objects.DataPacket;
import util.network.Client;
import util.state_machine.State;

public class TakeScreenshot implements State {
    private static final int AMOUNT_OF_FRAMES_TO_WAIT = 30;
    private static final int AMOUNT_OF_FRAMES_TO_TAKE_SCREENSHOT = 15;

    private int runCounter;

    @Override
    public void start(DataPacket input) {
        runCounter = 0;
    }

    @Override
    public void exec(DataPacket input) {
        runCounter++;
        if(runCounter == AMOUNT_OF_FRAMES_TO_TAKE_SCREENSHOT) {
            Client.send("take-screenshot");
        }
    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(runCounter >= AMOUNT_OF_FRAMES_TO_WAIT) {
            return new GetNextFrameData();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {

    }
}
