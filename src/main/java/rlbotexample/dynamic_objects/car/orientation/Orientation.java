package rlbotexample.dynamic_objects.car.orientation;


import rlbot.flat.PlayerInfo;
import util.math.vector.Vector3;

/**
 * The car's orientation in space, a.k.a. what direction it's pointing.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */public class Orientation {

    /** The direction that the front of the car is facing */
    public Vector3 noseVector;

    /** The direction the roof of the car is facing. (0, 0, 1) means the car is upright. */
    public Vector3 roofVector;

    /** The direction that the right side of the car is facing. */
    public Vector3 rightVector;

    public Orientation() {
        this.noseVector = Vector3.X_VECTOR;
        this.roofVector = Vector3.Z_VECTOR;
        this.rightVector = noseVector.crossProduct(roofVector);
    }

    public Orientation(Vector3 noseVector, Vector3 roofVector) {
        this.noseVector = noseVector;
        this.roofVector = roofVector;
        this.rightVector = noseVector.crossProduct(roofVector);
    }

    public static Orientation fromFlatbuffer(PlayerInfo playerInfo) {
        return convert(
                playerInfo.physics().rotation().pitch(),
                playerInfo.physics().rotation().yaw(),
                playerInfo.physics().rotation().roll());
    }

    /**
     * All params are in radians.
     */
    private static Orientation convert(double pitch, double yaw, double roll) {
        double noseX = -1 * Math.cos(pitch) * Math.cos(yaw);
        double noseY = Math.cos(pitch) * Math.sin(yaw);
        double noseZ = Math.sin(pitch);

        double roofX = Math.cos(roll) * Math.sin(pitch) * Math.cos(yaw) + Math.sin(roll) * Math.sin(yaw);
        double roofY = Math.cos(yaw) * Math.sin(roll) - Math.cos(roll) * Math.sin(pitch) * Math.sin(yaw);
        double roofZ = Math.cos(roll) * Math.cos(pitch);

        return new Orientation(new Vector3(noseX, noseY, noseZ), new Vector3(roofX, roofY, roofZ));
    }

    public Orientation rotate(Vector3 orientationRotator) {
        return new Orientation(
                noseVector.rotate(orientationRotator),
                roofVector.rotate(orientationRotator));
    }

    public Vector3 findAngularDisplacementTo(Orientation that) {
        final Vector3 noseRotationDisk = findRotationDisk(this.noseVector, that.noseVector);
        final Vector3 roofRotationDisk = findRotationDisk(this.roofVector, that.roofVector);
        final Vector3 directionOfDisplacementVector = computeDirectionOfDisplacementVector(noseRotationDisk, roofRotationDisk);
        final Vector3 flatteningRotator = directionOfDisplacementVector.findRotator(Vector3.Z_VECTOR);

        final double displacementVectorMagnitude;
        if(!noseRotationDisk.isZero()) {
            displacementVectorMagnitude = signedAngleBetweenDirectionsForASpecificRotator(that.noseVector, this.noseVector, flatteningRotator);
        }
        else {
            displacementVectorMagnitude = signedAngleBetweenDirectionsForASpecificRotator(that.roofVector, this.roofVector, flatteningRotator);
        }

        return directionOfDisplacementVector.scaledToMagnitude(displacementVectorMagnitude);
    }

    private Vector3 computeDirectionOfDisplacementVector(final Vector3 noseRotationDisk, final Vector3 roofRotationDisk) {
        final Vector3 directionOfDisplacementVector = noseRotationDisk.crossProduct(roofRotationDisk);

        if(directionOfDisplacementVector.isZero()) {
            if(!noseRotationDisk.isZero()) {
                return this.roofVector;
            }
            else if(!roofRotationDisk.isZero()) {
                return this.noseVector;
            }
        }

        return directionOfDisplacementVector;
    }

    private Vector3 findRotationDisk(final Vector3 direction1, final Vector3 direction2) {
        final Vector3 middleNoseVector = direction1.plus(direction2).mutableNormalized();
        return middleNoseVector.crossProduct(direction1).mutableCrossProduct(middleNoseVector);
    }

    private double signedAngleBetweenDirectionsForASpecificRotator(final Vector3 direction1, final Vector3 direction2, final Vector3 rotator) {
        final Vector3 rotatorCopy = new Vector3(rotator);
        return direction2.mutableParamRotate(rotatorCopy).flatten()
                .correctionAngle(direction1.mutableParamRotate(rotator).flatten());
    }

    public Vector3 asAngularDisplacement() {
        return new Orientation().findAngularDisplacementTo(this);
    }
}
