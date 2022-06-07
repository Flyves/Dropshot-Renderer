package rlbotexample.app.physics.game.states.tile_display;

import rlbotexample.app.physics.game.states.FrameData;
import rlbotexample.dynamic_objects.DataPacket;
import util.network.Client;
import util.state_machine.State;

import java.util.HashSet;

public class GetNextFrameData implements State {
    @Override
    public void start(DataPacket input) {
        if(FrameData.previousFrame == null) {
            FrameData.nextFrame = FrameData.computeTileIndexesToBreak(Client.sendAndReceive("request-current-frame"));
        }
        FrameData.previousFrame = FrameData.currentFrame;
        FrameData.currentFrame = FrameData.nextFrame;
        FrameData.isQueryingNextFrame = true;
        new Thread(() -> {
            FrameData.nextFrame = FrameData.computeTileIndexesToBreak(Client.sendAndReceive("request-next-frame"));
            FrameData.isQueryingNextFrame = false;
        }).start();
    }
    @Override
    public void exec(DataPacket input) {
    }

    @Override
    public void stop(DataPacket input) {
    }

    @Override
    public State next(DataPacket input) {
        if(new HashSet<>(FrameData.currentFrame).containsAll(FrameData.previousFrame)) {
            return new SetTileDifferences();
        }
        return new ResetBlue();
    }

    @Override
    public void debug(DataPacket input) {

    }
}
