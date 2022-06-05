package rlbotexample.asset.animation.car_group;

import util.math.vector.ZyxOrientedPosition;

import java.io.Serializable;

public class CarData implements Serializable {

    public int carId;
    public int teamId;
    public ZyxOrientedPosition zyxOrientedPosition;
    public boolean isBoosting;

    public CarData(int carId, int teamId, ZyxOrientedPosition zyxOrientedPosition, boolean isBoosting) {
        this.carId = carId;
        this.teamId = teamId;
        this.zyxOrientedPosition = zyxOrientedPosition;
        this.isBoosting = isBoosting;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CarData)) {
            return false;
        }
        return ((CarData)obj).carId == this.carId;
    }
}
