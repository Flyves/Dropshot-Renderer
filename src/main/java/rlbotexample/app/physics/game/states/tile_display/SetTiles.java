package rlbotexample.app.physics.game.states.tile_display;

import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.dynamic_objects.DataPacket;
import util.state_machine.State;

import java.util.ArrayList;
import java.util.List;

public class SetTiles implements State {
    private long timeToElapse;

    @Override
    public void start(DataPacket input) {
        timeToElapse = System.currentTimeMillis() + 4500;
        BallStateSetter.setIsBackgroundPositionEnabled(true);

        final List<Integer> remainingIndexes = new ArrayList<>();

        for(int i = 0; i < 140; i++) {
            remainingIndexes.add(i);
        }
        for(int i = 0; i < 140; i++) {
            int id1 = (int)(Math.random()*140);
            int id2 = (int)(Math.random()*140);
            int value1 = remainingIndexes.get(id1);
            int value2 = remainingIndexes.get(id2);
            remainingIndexes.set(id1, value2);
            remainingIndexes.set(id2, value1);
        }

        BallStateSetter.tileIndexesToBreak.addAll(remainingIndexes);
    }

    @Override
    public void exec(DataPacket input) {

    }

    @Override
    public void stop(DataPacket input) {

    }

    @Override
    public State next(DataPacket input) {
        if(minResetTimeElapsed() && BallStateSetter.tileIndexesToBreak.isEmpty()) {
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
