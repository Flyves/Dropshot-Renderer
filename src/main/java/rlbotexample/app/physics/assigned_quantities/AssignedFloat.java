package rlbotexample.app.physics.assigned_quantities;

import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.Vector3;

public class AssignedFloat {

    public ExtendedCarData carData;
    public float value;

    public AssignedFloat(ExtendedCarData carData, float value) {
        this.carData = carData;
        this.value = value;
    }
}