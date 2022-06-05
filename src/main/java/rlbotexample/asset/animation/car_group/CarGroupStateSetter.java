package rlbotexample.asset.animation.car_group;

import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.asset.animation.animation.DemolitionStateReseter;
import rlbotexample.asset.animation.animation.Animation;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.resource_handling.cars.CarResourceHandler;

import java.util.*;
import java.util.stream.Collectors;

public class CarGroupStateSetter {
    private final Map<Integer, Integer> animationIndexToInGameCarIndexMap;
    private boolean isClosed;

    public CarGroupStateSetter(final Animation animation) {
        this.animationIndexToInGameCarIndexMap = new HashMap<>();
        this.isClosed = false;
        alloc(animation);
    }

    private void alloc(final Animation animation) {
        if(animation.frames.size() == 0) {
            return;
        }

        // finding the teams
        final List<Integer> teamIds = animation.frames.get(0).carGroup.carObjects.stream()
                .map(carObject -> carObject.teamId)
                .collect(Collectors.toList());
        // allocating the right car colors using team ids
        final Optional<List<Integer>> allocatedCarsOpt = CarResourceHandler.alloc(teamIds);

        // linking references
        allocatedCarsOpt.ifPresent(indexes -> {
            for(int i = 0; i < animation.frames.get(0).carGroup.carObjects.size(); i++) {
                final Integer inGameCarIndex = indexes.get(i);
                final Integer animationCarIndex = i;
                animationIndexToInGameCarIndexMap.put(animationCarIndex, inGameCarIndex);
            }
        });
        // making sure we have enough cars of each colors
        if(teamIds.size() != animationIndexToInGameCarIndexMap.size()) {
            throw new RuntimeException("not enough cars to load the animation.\n" +
                    "Expected " + animation.frames.get(0).carGroup.amountOfBlueCars() + "b and "
                    + animation.frames.get(0).carGroup.amountOfOrangeCars() + "r");
        }
    }

    public void stateSet(final CarGroup carGroup, final DataPacket input) {
        animationIndexToInGameCarIndexMap.forEach((k, v) -> {
            final ExtendedCarData carToStateSet = input.allCars.get(v);
            if(carToStateSet.isDemolished) {
                DemolitionStateReseter.stateSet(carToStateSet);
            }
            else {
                final CarData carData = carGroup.carObjects.get(k);
                PhysicsOfBossBattle.setZyxOrientedPosition(carData.zyxOrientedPosition, carToStateSet);
                PhysicsOfBossBattle.setBoostAmount(50, carToStateSet);
                PhysicsOfBossBattle.setIsBoosting(carData.isBoosting, carToStateSet);
            }
        });
    }

    public void close() {
        if(!isClosed) {
            CarResourceHandler.free(animationIndexToInGameCarIndexMap.values());
            isClosed = true;
        }
    }
}