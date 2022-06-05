package rlbotexample.asset.animation.car_group;

import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.OrientedPosition;
import util.math.vector.Vector3;
import util.math.vector.ZyxOrientedPosition;

public abstract class CarGroupUtils {
    public static CarGroup interpolate(final CarGroup carGroup1, final CarGroup carGroup2, final double t) {
        final CarGroup interpolatedCarGroup = new CarGroup();

        for(int i = 0; i < carGroup1.carObjects.size(); i++) {
            // oriented positions
            final OrientedPosition orientedPosition1 = carGroup1.carObjects.get(i).zyxOrientedPosition.toCarOrientedPosition();
            final OrientedPosition orientedPosition2 = carGroup2.carObjects.get(i).zyxOrientedPosition.toCarOrientedPosition();

            // linear interpolation
            final Vector3 newPosition = orientedPosition1.position.scaled(1-t).plus(orientedPosition2.position.scaled(t));
            final Vector3 rotation = orientedPosition1.orientation.findAngularDisplacementTo(orientedPosition2.orientation);
            final Orientation newOrientation = orientedPosition1.orientation.rotate(rotation.scaled(t));
            final ZyxOrientedPosition resultingOrientedPosition = new OrientedPosition(newPosition, newOrientation).toZyxOrientedPosition();

            // boosting
            final boolean isBoosting;
            if(carGroup1.carObjects.get(i).isBoosting == carGroup2.carObjects.get(i).isBoosting) {
                isBoosting = carGroup1.carObjects.get(i).isBoosting;
            }
            else {
                isBoosting = t > 0.5 ? carGroup2.carObjects.get(i).isBoosting : carGroup1.carObjects.get(i).isBoosting;
            }


            // constructing the object
            final int carId = carGroup1.carObjects.get(i).carId;
            final int teamId = carGroup1.carObjects.get(i).teamId;
            final CarData interpolatedCarData = new CarData(carId, teamId, resultingOrientedPosition, isBoosting);
            interpolatedCarGroup.carObjects.add(interpolatedCarData);
        }

        return interpolatedCarGroup;
    }

    public static CarGroup addOffset(final CarGroup carGroup, OrientedPosition offset) {
        final CarGroup updatedCarGroup = new CarGroup();

        for(int i = 0; i < carGroup.carObjects.size(); i++) {
            final OrientedPosition carGroupOrientedPosition = carGroup.carObjects.get(i).zyxOrientedPosition.toCarOrientedPosition();
            final ZyxOrientedPosition result = carGroupOrientedPosition.toGlobalPosition(offset).toZyxOrientedPosition();
            final int carId = carGroup.carObjects.get(i).carId;
            final int teamId = carGroup.carObjects.get(i).teamId;
            final boolean isBoosting = carGroup.carObjects.get(i).isBoosting;
            updatedCarGroup.carObjects.add(new CarData(carId, teamId, result, isBoosting));
        }

        return updatedCarGroup;
    }
}