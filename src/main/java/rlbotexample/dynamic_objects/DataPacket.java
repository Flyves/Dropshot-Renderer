package rlbotexample.dynamic_objects;

import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.FieldInfo;
import rlbot.flat.GameTickPacket;
import rlbot.gamestate.GameInfoState;
import rlbot.gamestate.GameState;
import rlbotexample.dropshot_tiles.DropshotTileLocations;
import rlbotexample.dynamic_objects.ball.BallData;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.Vector3;
import util.resource_handling.cars.CarResourceHandler;
import util.resource_handling.cars.PlayerIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it. The benefits of using this instead of rlbot.flat.GameTickPacket are:
 * 1. You end up with nice custom Vector3 objects that you can call methods on.
 * 2. If the framework changes its data format, you can just update the code here
 * and leave your bot logic alone.
 */
public class DataPacket {

    /** Your own car, based on the playerIndex */
    public final ExtendedCarData car;
    public final List<ExtendedCarData> allCars;

    public final BallData ball;
    public final int team;

    /** The index of your player */
    public final int botIndex;
    public int humanIndex = 0;

    public final float gameSpeed;

    /** The index of the bot that is going to reload the ball prediction (if there is many bots) */
    public static boolean carResourceHandlerHasBeenInitialized = false;
    public final List<Vector3> dropshotTilePositions;
    public final int blueMinusOrangeGoals;

    public DataPacket(
            final GameTickPacket request,
            final AtomicReference<Optional<DataPacket>> previousDataPacketOptRef,
            final int playerIndex) {
        gameSpeed = request.gameInfo().gameSpeed();
        blueMinusOrangeGoals = request.teams(0).score() - request.teams(1).score();

        this.botIndex = playerIndex;
        this.allCars = new ArrayList<>();
        Optional<DataPacket> previousInput = previousDataPacketOptRef.get();

        for (int i = 0; i < request.playersLength(); i++) {
            final rlbot.flat.PlayerInfo playerInfo = request.players(i);
            final float elapsedSeconds = request.gameInfo().secondsElapsed();
            final AtomicReference<Optional<ExtendedCarData>> previousCarOptRef = new AtomicReference<>(Optional.empty());
            int finalI = i;
            previousInput.ifPresent(previousInputPresent -> previousCarOptRef.set(Optional.of(previousInputPresent.allCars.get(finalI))));

            allCars.add(new ExtendedCarData(playerInfo, previousCarOptRef.get(), i, elapsedSeconds));
            allCars.get(i).previousCarData.ifPresent(previousCarData -> previousCarData.previousCarData = Optional.empty());
        }

        dropshotTilePositions = new ArrayList<>(DropshotTileLocations.locations);

        this.car = allCars.get(playerIndex);
        this.team = this.car.team;
        this.ball = new BallData(request.ball());

        loadData(request);
    }

    private void loadData(GameTickPacket request) {
        // update the "raw" ball trajectory
        //RawBallTrajectory.update(ball);

        // refresh boostPads information so we can utilize it
        //BoostManager.loadGameTickPacket(request);

        if(!carResourceHandlerHasBeenInitialized) {
            CarResourceHandler.initialize(allCars);
            //CarResourceHandler.alloc(new PlayerIndex(humanCar.playerIndex));
            carResourceHandlerHasBeenInitialized = true;
        }
    }
}
