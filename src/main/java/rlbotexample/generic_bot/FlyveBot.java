package rlbotexample.generic_bot;

import rlbot.render.Renderer;
import rlbotexample.dynamic_objects.DataPacket;
import util.game_constants.RlConstants;
import util.renderers.RenderTasks;

import java.awt.*;

public abstract class FlyveBot extends BotBehaviour {

    @Override
    public void updateGui(DataPacket input, double currentFps, double averageFps, long botExecutionTime) {}
}
