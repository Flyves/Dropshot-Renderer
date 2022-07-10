package rlbotexample;

import rlbot.Bot;
import rlbot.manager.BotManager;
import rlbot.pyinterop.SocketServer;
import rlbotexample.app.TileRendererBot;

public class SamplePythonInterface extends SocketServer {

    private BotManager botManager;

    public SamplePythonInterface(int port, BotManager botManager) {
        super(port, botManager);
        this.botManager = botManager;
    }

    protected Bot initBot(int index, String botType, int team) {
        return new SampleBot(index, new TileRendererBot(), botManager);
    }
}
