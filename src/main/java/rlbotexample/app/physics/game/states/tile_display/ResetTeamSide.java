package rlbotexample.app.physics.game.states.tile_display;

import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

public class ResetTeamSide implements State {
    private long timeToElapse;
    private final int tileIndex;
    private final State nextState;

    public ResetTeamSide(final int tileIndexForReset, final State nextState) {
        this.tileIndex = tileIndexForReset;
        this.nextState = nextState;
    }

    @Override
    public void start(DataPacket input) {
        timeToElapse = System.currentTimeMillis() + 900;
        BallStateSetter.tileIndexesToBreak.add(tileIndex);
        BallStateSetter.tileIndexesToBreak.add(tileIndex);
        BallStateSetter.tileIndexesToBreak.add(tileIndex);
        BallStateSetter.tileIndexesToBreak.add(tileIndex);
        BallStateSetter.setIsBackgroundPositionEnabled(false);
    }

    @Override
    public void exec(DataPacket input) {

    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(timeElapsed()) {
            BallStateSetter.tileIndexesToBreak.clear();
            return nextState;
        }
        return this;
    }

    private boolean timeElapsed() {
        return timeToElapse - System.currentTimeMillis() <= 0;
    }

    @Override
    public void debug(DataPacket input) {

    }
}
