package rlbotexample.app.physics.game.states.tile_display;

import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class TakeScreenshot implements State {
    private static final int AMOUNT_OF_FRAMES_TO_WAIT = 10;

    private int runCounter;

    @Override
    public void start(DataPacket input) {
        runCounter = 0;
    }

    @Override
    public void exec(DataPacket input) {
        runCounter++;
        if(runCounter == AMOUNT_OF_FRAMES_TO_WAIT) {
            // TODO: take a screenshot of rocket league

        }
    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(runCounter >= AMOUNT_OF_FRAMES_TO_WAIT) {
            return new ResetBlue();
        }
        return this;
    }

    @Override
    public void debug(DataPacket input) {

    }
}
