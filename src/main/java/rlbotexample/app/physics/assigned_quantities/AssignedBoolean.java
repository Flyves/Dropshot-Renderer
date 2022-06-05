package rlbotexample.app.physics.assigned_quantities;

import rlbotexample.dynamic_objects.car.ExtendedCarData;

public class AssignedBoolean {

    public ExtendedCarData carData;
    public boolean status;

    public AssignedBoolean(ExtendedCarData carData, boolean status) {
        this.carData = carData;
        this.status = status;
    }
}