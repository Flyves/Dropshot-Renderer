package util.resource_handling.cars;

import rlbotexample.app.physics.PhysicsOfBossBattle;
import rlbotexample.dynamic_objects.DataPacket;
import rlbotexample.dynamic_objects.car.ExtendedCarData;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;

import java.util.*;
import java.util.stream.Collectors;

public class CarResourceHandler {

    public static final List<Integer> freeCarIndexes = new ArrayList<>();
    private static final Map<Integer, Integer> carIdToTeamId = new HashMap<>();
    private static int rotatingIndexForFreeCars = 0;

    public static void initialize(List<ExtendedCarData> allCars) {
        freeCarIndexes.addAll(allCars.stream()
                .map(carData -> carData.playerIndex)
                .collect(Collectors.toList()));
        allCars.forEach(carData ->
                carIdToTeamId.put(carData.playerIndex, allCars.get(carData.playerIndex).team));
    }

    public static Optional<Integer> alloc(PlayerIndex playerIndexObj) {
        int playerIndex = playerIndexObj.value;

        if(freeCarIndexes.contains(playerIndex)) {
            freeCarIndexes.remove(playerIndex);
            return Optional.of(playerIndex);
        }
        return Optional.empty();
    }

    public static boolean free(PlayerIndex playerIndexObj) {
        int playerIndex = playerIndexObj.value;

        if(!freeCarIndexes.contains(playerIndex)) {
            freeCarIndexes.add(playerIndex);
            return true;
        }
        return false;
    }

    public static Optional<List<Integer>> alloc(PlayerAmount requestedAmount) {
        if(freeCarIndexes.size() < requestedAmount.value) {
            return Optional.empty();
        }

        List<Integer> allocatedCarIndexes = new ArrayList<>(requestedAmount.value);

        for(int i = freeCarIndexes.size() - requestedAmount.value; i < freeCarIndexes.size(); i++) {
            allocatedCarIndexes.add(freeCarIndexes.get(i));
        }
        freeCarIndexes.removeAll(allocatedCarIndexes);

        return Optional.of(allocatedCarIndexes);
    }

    public static void free(final Collection<Integer> requestedCarIndexes) {
        requestedCarIndexes.stream()
                .map(PlayerIndex::new)
                .forEach(CarResourceHandler::free);
    }

    public static Optional<List<Integer>> alloc(List<Integer> teamIds) {
        List<Integer> blueTeamIndexes = freeCarIndexes.stream()
                .filter(index -> 0 == carIdToTeamId.get(index))
                .collect(Collectors.toList());
        List<Integer> orangeTeamIndexes = freeCarIndexes.stream()
                .filter(index -> 1 == carIdToTeamId.get(index))
                .collect(Collectors.toList());

        int requestedOrangeAmount = teamIds.stream()
                .mapToInt(teamId -> teamId)
                .sum();
        int requestedBlueAmount = teamIds.size() - requestedOrangeAmount;

        if(blueTeamIndexes.size() < requestedBlueAmount
                || orangeTeamIndexes.size() < requestedOrangeAmount) {
            return Optional.empty();
        }

        List<Integer> orderedAllocatedCars = new ArrayList<>(teamIds.size());
        teamIds.forEach(teamId -> {
            if(teamId == 0) {
                int i = blueTeamIndexes.size()-1;
                orderedAllocatedCars.add(blueTeamIndexes.get(i));
                blueTeamIndexes.remove(i);
            }
            else {
                int i = orangeTeamIndexes.size()-1;
                orderedAllocatedCars.add(orangeTeamIndexes.get(i));
                orangeTeamIndexes.remove(i);
            }
        });

        freeCarIndexes.removeAll(orderedAllocatedCars);

        return Optional.of(orderedAllocatedCars);
    }

    public static List<ExtendedCarData> dereferenceIndexes(DataPacket input, List<Integer> carIndexes) {
        return carIndexes.stream()
                .map(input.allCars::get)
                .collect(Collectors.toList());
    }

    public static void handleFreeCars(DataPacket input) {
        final List<ExtendedCarData> freeCars = dereferenceIndexes(input, freeCarIndexes);
        final ExtendedCarData dereferencedCar = getRotatingFreeCarReference(freeCars);
        if(dereferencedCar == null) {
            return;
        }
        PhysicsOfBossBattle.setZyxOrientedPosition(new OrientedPosition(new Vector3(30000, dereferencedCar.playerIndex*200, 50000), new Orientation()).toZyxOrientedPosition(), dereferencedCar);
    }

    private static ExtendedCarData getRotatingFreeCarReference(final List<ExtendedCarData> freeCars) {
        if(freeCars.isEmpty()) {
            return null;
        }
        if(rotatingIndexForFreeCars >= freeCars.size()) {
            rotatingIndexForFreeCars = 0;
        }
        final ExtendedCarData referenceToReturn = freeCars.get(rotatingIndexForFreeCars);
        rotatingIndexForFreeCars++;
        return referenceToReturn;
    }
}