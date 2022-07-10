package rlbotexample.app.physics.state_setter;

import rlbot.gamestate.*;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.state_setting.GameStateHelper;

public class CarStateSetter {

    public static void applyPhysics(PhysicsState alternativePhysics, float boostAmount, ExtendedCarData carData, DataPacket input) {
        GameState gameState = GameStateHelper.getCurrentGameState();
        final CarState defaultCarState = new CarState();
        final CarState alternativeCarState = defaultCarState.withPhysics(alternativePhysics);
        if(carData.isBot) {
            defaultCarState.withBoostAmount(boostAmount);
        }
        gameState.withCarState(carData.playerIndex, alternativeCarState);

        gameState.withGameInfoState(new GameInfoState().withGameSpeed(100f));
        GameStateHelper.applyGameState(gameState);
    }

}