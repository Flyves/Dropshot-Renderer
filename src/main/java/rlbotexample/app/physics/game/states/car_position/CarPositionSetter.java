package rlbotexample.app.physics.game.states.car_position;

import rlbotexample.app.physics.ModifiedPhysics;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.orientation.Orientation;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.resource_handling.cars.CarResourceHandler;
import util.resource_handling.cars.PlayerAmount;
import util.state_machine.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarPositionSetter implements State {
    private final List<Integer> carIndexes;

    public CarPositionSetter() {
        carIndexes = new ArrayList<>();
    }

    @Override
    public void start(DataPacket input) {
        Optional<List<Integer>> carsOpt = CarResourceHandler.alloc(new PlayerAmount(1));
        if(!carsOpt.isPresent()) {
            throw new RuntimeException("alloc: couldn't alloc enough cars to execute the script.");
        }
        carIndexes.addAll(carsOpt.get());
    }

    @Override
    public void exec(DataPacket input) {
        final List<ExtendedCarData> cars = CarResourceHandler.dereferenceIndexes(input, carIndexes);
        ModifiedPhysics.setVelocity(new Vector3(), cars.get(0));
        ModifiedPhysics.setOrientedPosition(new OrientedPosition(
                new Vector3(0, 0, 6000),
                new Orientation(Vector3.Z_VECTOR.plus(new Vector3(0.00001, 0, 0)), Vector3.Y_VECTOR)),
                cars.get(0));
    }

    @Override
    public void stop(DataPacket input) {
        CarResourceHandler.free(carIndexes);
    }

    @Override
    public State next(DataPacket input) {
        return this;
    }

    @Override
    public void debug(DataPacket input) {

    }
}
