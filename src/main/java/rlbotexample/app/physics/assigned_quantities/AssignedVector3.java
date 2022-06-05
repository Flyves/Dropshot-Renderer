package rlbotexample.app.physics.assigned_quantities;

import rlbotexample.dynamic_objects.car.ExtendedCarData;
import util.math.vector.Vector3;

public class AssignedVector3 {

    public ExtendedCarData carData;
    public Vector3 vector;

    public AssignedVector3(ExtendedCarData carData, Vector3 vector) {
        this.carData = carData;
        this.vector = vector;
    }
}