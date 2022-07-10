package rlbotexample.app;

import rlbot.flat.GameTickPacket;
import rlbotexample.generic_bot.FlyveBot;
import rlbotexample.app.physics.ModifiedPhysics;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.generic_bot.output.BotOutput;

public class TileRendererBot extends FlyveBot {

    public TileRendererBot() {}

    @Override
    public BotOutput processInput(DataPacket input,  GameTickPacket packet) {
        ModifiedPhysics.execute(input);
        return new BotOutput();
    }

    @Override
    public void updateGui(DataPacket input, double currentFps, double averageFps, long botExecutionTime) {

    }
}