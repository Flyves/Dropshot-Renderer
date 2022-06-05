package rlbotexample.generic_bot;

import rlbot.flat.GameTickPacket;
import rlbot.render.Renderer;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.generic_bot.output.BotOutput;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BotBehaviour {
    public abstract BotOutput processInput(DataPacket input, GameTickPacket packet);
    public abstract void updateGui(DataPacket input, double currentFps, double averageFps, long botExecutionTime);
}
