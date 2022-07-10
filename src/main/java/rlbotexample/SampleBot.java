package rlbotexample;

import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;
import rlbot.manager.BotManager;
import rlbotexample.app.physics.ModifiedPhysics;
import rlbotexample.app.physics.state_setter.BallStateSetter;
import rlbotexample.generic_bot.BotBehaviour;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.generic_bot.output.BotOutput;
import rlbotexample.generic_bot.output.ControlsOutput;
import util.game_constants.RlConstants;
import util.network.Client;
import util.renderers.IndexedRenderer;
import util.renderers.RenderTasks;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class SampleBot implements Bot {

    private static final int AMOUNT_OF_BOTS_TO_KEEP_ALIVE = 60;

    private final int playerIndex;
    private BotOutput botOutput;
    private final BotBehaviour botBehaviour;
    public double averageFps;
    private long currentFpsTime;
    private long previousFpsTime;
    private long time1;
    private long time2;
    private long deltaTime;
    public double currentFps;
    public BotManager botManager;

    private boolean isAnimationsLoaderStarted;

    private final AtomicReference<Optional<DataPacket>> previousDataPacketOptRef;

    public static int amountOfBotsRunning = 0;

    public SampleBot(int playerIndex, BotBehaviour botBehaviour, BotManager botManager) {
        this.botManager = botManager;
        this.playerIndex = playerIndex;
        this.botOutput = new BotOutput();
        this.botBehaviour = botBehaviour;
        this.averageFps = 0;
        this.currentFpsTime = 0;
        this.previousFpsTime = 0;
        this.time1 = 0;
        this.time2 = 0;
        this.deltaTime = 0;
        this.currentFps = 0;

        this.isAnimationsLoaderStarted = false;

        this.previousDataPacketOptRef = new AtomicReference<>(Optional.empty());

        amountOfBotsRunning++;
        RenderTasks.init();
        try {
            Client.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static IndexedRenderer getNewIndexedRenderer() {
        return new IndexedRenderer(0);
    }

    /**
     * This is where we keep the actual bot logic. This function shows how to chase the getNativeBallPrediction.
     * Modify it to make your bot smarter!
     */
    private ControlsOutput processInput(DataPacket input, GameTickPacket packet) {
        // used at the beginning to load animations (crucial feature to prevent startup stalling)
        //handleInitialAnimationLoading();

        // compute boss + player interactions
        executeBot(input, packet);

        // render everything that's been computed
        //renderGui();
        //executeAnimationRequests(input);
        applyPhysics(input);

        // crude in-game debug data
        fpsDataCalc();
        //System.out.println(averageFps);
        return botOutput.getForwardedOutput();
    }

    private void applyPhysics(DataPacket input) {
        ModifiedPhysics.applyImpulses(input);
        BallStateSetter.handleBallState(input);
    }

    private void renderGui() {
        RenderTasks.init();
        RenderTasks.render();
        RenderTasks.clearTaskBuffer();
    }

    private void executeBot(DataPacket input, GameTickPacket packet) {
        botOutput = botBehaviour.processInput(input, packet);
        botBehaviour.updateGui(input, currentFps, averageFps, deltaTime);
    }

    private void fpsDataCalc() {
        previousFpsTime = currentFpsTime;
        currentFpsTime = System.currentTimeMillis();

        if(currentFpsTime - previousFpsTime == 0) {
            currentFpsTime++;
        }
        currentFps = 1.0 / ((currentFpsTime - previousFpsTime) / 1000.0);
        averageFps = (averageFps*(RlConstants.BOT_REFRESH_RATE-1) + (currentFps)) / RlConstants.BOT_REFRESH_RATE;
    }

    @Override
    public int getIndex() {
        return this.playerIndex;
    }

    /**
     * This is the most important function. It will automatically get called by the framework with fresh data
     * every frame. Respond with appropriate controls!
     */
    @Override
    public ControllerState processInput(GameTickPacket packet) {
        // timestamp before executing the bot
        time1 = System.currentTimeMillis();

        DataPacket dataPacket = new DataPacket(packet, previousDataPacketOptRef, playerIndex);
        ControlsOutput controlsOutput = processInput(dataPacket, packet);

        previousDataPacketOptRef.set(Optional.of(dataPacket));

        // timestamp after executing the bot
        time2 = System.currentTimeMillis();

        deltaTime = time2 - time1;
        return controlsOutput;
    }

    public void retire() {
        //System.out.println("Retiring bot " + playerIndex);
        RenderTasks.close();
    }

    public void killBot() {
        botManager.retireBot(playerIndex);
        amountOfBotsRunning--;
    }
}
