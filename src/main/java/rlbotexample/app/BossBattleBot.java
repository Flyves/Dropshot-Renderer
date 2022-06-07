package rlbotexample.app;

import rlbot.flat.GameTickPacket;
import rlbotexample.generic_bot.FlyveBot;
import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.generic_bot.output.BotOutput;

public class BossBattleBot extends FlyveBot {

    public BossBattleBot() {}

    @Override
    public BotOutput processInput(DataPacket input,  GameTickPacket packet) {
        PhysicsOfBossBattle.execute(input);
        return new BotOutput();
    }

    @Override
    public void updateGui(DataPacket input, double currentFps, double averageFps, long botExecutionTime) {
        //super.updateGui(input, currentFps, averageFps, botExecutionTime);
    }
}