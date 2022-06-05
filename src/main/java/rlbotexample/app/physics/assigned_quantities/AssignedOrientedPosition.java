package rlbotexample.app.physics.assigned_quantities;

import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.ZyxOrientedPosition;

public class AssignedOrientedPosition {

    public ExtendedCarData carData;
    public ZyxOrientedPosition orientedPosition;

    public AssignedOrientedPosition(ExtendedCarData carData, ZyxOrientedPosition orientedPosition) {
        this.carData = carData;
        this.orientedPosition = orientedPosition;
    }
}