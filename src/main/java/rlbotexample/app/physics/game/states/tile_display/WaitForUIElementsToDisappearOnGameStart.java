package rlbotexample.app.physics.game.states.tile_display;

import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class WaitForUIElementsToDisappearOnGameStart implements State {
    private static final int MIN_DELAY = 15000;

    private long timeToElapse;

    @Override
    public void start(DataPacket input) {
        timeToElapse = System.currentTimeMillis() + MIN_DELAY;
        System.out.println("Waiting for UI elements to disappear...");
    }

    @Override
    public void exec(DataPacket input) {

    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(minResetTimeElapsed()) {
            return new GetNextFrameData();
        }
        return this;
    }

    private boolean minResetTimeElapsed() {
        return timeToElapse - System.currentTimeMillis() <= 0;
    }

    @Override
    public void debug(DataPacket input) {

    }
}
