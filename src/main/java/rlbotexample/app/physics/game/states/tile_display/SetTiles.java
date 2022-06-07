package rlbotexample.app.physics.game.states.tile_display;

import rlbotexample.app.physics.game.states.FrameData;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class SetTiles implements State {
    private static final int MIN_DELAY = 5000;

    private long timeToElapse;

    @Override
    public void start(DataPacket input) {
        timeToElapse = System.currentTimeMillis() + MIN_DELAY;
        BallStateSetter.setIsBackgroundPositionEnabled(true);

        BallStateSetter.tileIndexesToBreak.addAll(FrameData.currentFrame);
    }

    @Override
    public void exec(DataPacket input) {

    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(minResetTimeElapsed() && BallStateSetter.tileIndexesToBreak.isEmpty() && !FrameData.isQueryingNextFrame) {
            return new TakeScreenshot();
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
