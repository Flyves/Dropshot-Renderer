package rlbotexample.app.physics;

import rlbot.gamestate.DesiredVector3;
import rlbot.gamestate.PhysicsState;
import rlbotexample.app.physics.assigned_quantities.AssignedBoolean;
import rlbotexample.app.physics.assigned_quantities.AssignedFloat;
import rlbotexample.app.physics.assigned_quantities.AssignedOrientedPosition;
import rlbotexample.app.physics.assigned_quantities.AssignedVector3;
import rlbotexample.app.physics.game.GameModMachine;
import rlbotexample.app.physics.state_setter.CarStateSetter;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.game_constants.RlConstants;
import util.math.vector.OrientedPosition;
import util.math.vector.ZyxOrientedPosition;
import util.math.vector.Vector3;
import util.resource_handling.cars.CarResourceHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModifiedPhysics {

    private static final List<AssignedOrientedPosition> assignedOrientedPositions = new ArrayList<>();
    private static final List<AssignedVector3> assignedVelocities = new ArrayList<>();
    private static final List<AssignedVector3> assignedSpins = new ArrayList<>();
    private static final List<AssignedVector3> assignedAccelerations = new ArrayList<>();
    private static final List<AssignedVector3> assignedPenetrations = new ArrayList<>();
    private static final List<AssignedFloat> assignedBoostAmounts = new ArrayList<>();
    private static final List<AssignedBoolean> assignedIsBoosting = new ArrayList<>();

    public static void execute(DataPacket input) {
        GameModMachine.step(input);

        CarResourceHandler.handleFreeCars(input);
    }

    public static void setZyxOrientedPosition(ZyxOrientedPosition orientedPosition, ExtendedCarData carData) {
        assignedOrientedPositions.add(new AssignedOrientedPosition(carData, orientedPosition));
    }

    public static void setOrientedPosition(OrientedPosition orientedPosition, ExtendedCarData carData) {
        assignedOrientedPositions.add(new AssignedOrientedPosition(carData, orientedPosition.toZyxOrientedPosition()));
    }

    public static void setVelocity(Vector3 newVelocity, ExtendedCarData carData) {
        assignedVelocities.add(new AssignedVector3(carData, newVelocity));
    }

    public static void setIsBoosting(boolean isBoosting, ExtendedCarData carToStateSet) {
        removeAssignedIsBoostingIfPresent(carToStateSet);
        assignedIsBoosting.add(new AssignedBoolean(carToStateSet, isBoosting));
    }

    public static void setBoostAmount(float boostAmount, ExtendedCarData carToStateSet) {
        assignedBoostAmounts.add(new AssignedFloat(carToStateSet, boostAmount));
    }

    public static void setSpin(Vector3 newSpin, ExtendedCarData carData) {
        assignedSpins.add(new AssignedVector3(carData, newSpin));
    }

    // all forces are applied to the center of mass for now
    public static void addImpulse(Vector3 force, ExtendedCarData carData) {
        assignedAccelerations.add(new AssignedVector3(carData, force.scaled(1 / RlConstants.CAR_MASS)));
    }

    public static void addAcceleration(Vector3 acceleration, ExtendedCarData carData) {
        assignedAccelerations.add(new AssignedVector3(carData, acceleration));
    }

    public static void addPenetrationError(Vector3 penetration, ExtendedCarData carData) {
        assignedPenetrations.add(new AssignedVector3(carData, penetration));
    }

    public static void applyImpulses(DataPacket input) {
        for(ExtendedCarData car: input.allCars) {
            Optional<ZyxOrientedPosition> orientedPositionOpt;
            Optional<Vector3> velocityOpt;
            Optional<Vector3> spinOpt;
            Optional<Vector3> accelerationOpt;
            Optional<Vector3> penetrationOpt;

            final PhysicsState alternativePhysics = new PhysicsState();

            orientedPositionOpt = assignedOrientedPositions.stream()
                    .filter(assignedOrientedPosition -> assignedOrientedPosition.carData == car)
                    .map(assignedOrientedPosition -> assignedOrientedPosition.orientedPosition)
                    .findFirst();
            velocityOpt = assignedVelocities.stream()
                    .filter(assignedVector3 -> assignedVector3.carData == car)
                    .map(assignedVector3 -> assignedVector3.vector)
                    .findFirst();
            spinOpt = assignedSpins.stream()
                    .filter(assignedVector3 -> assignedVector3.carData == car)
                    .map(assignedVector3 -> assignedVector3.vector)
                    .findFirst();

            accelerationOpt = assignedAccelerations.stream()
                    .filter(assignedVector3 -> assignedVector3.carData == car)
                    .map(assignedVector3 -> assignedVector3.vector)
                    .findFirst();
            penetrationOpt = assignedPenetrations.stream()
                    .filter(assignedVector3 -> assignedVector3.carData == car)
                    .map(assignedVector3 -> assignedVector3.vector)
                    .findFirst();
            float boostAmount = assignedBoostAmounts.stream()
                    .filter(assignedBoolean -> assignedBoolean.carData == car)
                    .map(assignedBoolean -> assignedBoolean.value)
                    .findFirst()
                    .orElse((float)car.boost);

            final double dt = 1/RlConstants.BOT_REFRESH_RATE;
            penetrationOpt.ifPresent(penetration -> {
                final Vector3 newLocation = car.position.minus(penetration);
                final DesiredVector3 newLocationAsDesiredVector3 = newLocation.toFlippedDesiredVector3();
                alternativePhysics.withLocation(newLocationAsDesiredVector3);
            });

            velocityOpt.ifPresent(velocity -> {
                alternativePhysics.withVelocity(velocity.toFlippedDesiredVector3());
            });

            spinOpt.ifPresent(spin -> alternativePhysics.withAngularVelocity(spin.toDesiredVector3()));

            orientedPositionOpt.ifPresent(orientedPosition -> {
                if(!penetrationOpt.isPresent()) {
                    alternativePhysics.withLocation(orientedPosition.position.toFlippedDesiredVector3());
                }
                else {
                    alternativePhysics.withLocation(orientedPosition.position.minus(penetrationOpt.get()).toFlippedDesiredVector3());
                }
                alternativePhysics.withRotation(orientedPosition.eulerZYX.toDesiredRotation());
                if(!velocityOpt.isPresent() && !accelerationOpt.isPresent()) {
                    alternativePhysics.withVelocity(new Vector3(0, 0, 0).toFlippedDesiredVector3());
                }
            });

            accelerationOpt.ifPresent(acceleration -> {
                final Vector3 newVelocity = car.velocity.plus(acceleration.scaled(dt));
                final DesiredVector3 newVelocityAsDesiredVector3 = newVelocity.toFlippedDesiredVector3();
                alternativePhysics.withVelocity(newVelocityAsDesiredVector3);
            });

            CarStateSetter.applyPhysics(alternativePhysics, boostAmount, car, input);
        }

        assignedOrientedPositions.clear();
        assignedVelocities.clear();
        assignedSpins.clear();
        assignedAccelerations.clear();
        assignedPenetrations.clear();
        assignedBoostAmounts.clear();
    }

    public static boolean shouldBeBoosting(int playerIndex) {
        synchronized (assignedIsBoosting) {
            try {
                return assignedIsBoosting.stream()
                        .anyMatch(assignedBoolean -> assignedBoolean.carData.playerIndex == playerIndex && assignedBoolean.status);
            }
            catch (final RuntimeException e){
                return false;
            }
        }
    }

    private static void removeAssignedIsBoostingIfPresent(ExtendedCarData carToStateSet) {
        if(assignedIsBoosting.stream().anyMatch(assignedBoolean -> assignedBoolean.carData.playerIndex == carToStateSet.playerIndex)) {
            synchronized (assignedIsBoosting) {
                final List<AssignedBoolean> assignedBooleansToRemove = new ArrayList<>();
                assignedIsBoosting.stream()
                        .filter(assignedBoolean -> assignedBoolean.carData.playerIndex == carToStateSet.playerIndex)
                        .findFirst()
                        .ifPresent(assignedBooleansToRemove::add);
                assignedIsBoosting.removeAll(assignedBooleansToRemove);
            }
        }
    }
}