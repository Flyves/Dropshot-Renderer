package util.math.vector;

import util.math.orientation.Orientation;

import java.io.Serializable;

public class OrientedPosition implements Serializable {
    public Vector3 position;
    public Orientation orientation;

    public OrientedPosition() {
        this.position = new Vector3();
        this.orientation = new Orientation();
    }

    public OrientedPosition(Vector3 position, Orientation orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    public ZyxOrientedPosition toZyxOrientedPosition() {
        final Vector3 restOrientation = Vector3.X_VECTOR;
        Vector3 flatFront = new Vector3(orientation.noseVector.flatten(), 0);
        if(flatFront.isZero()) {
            flatFront = restOrientation;
        }
        final Vector3 rotatorZ = flatFront.findRotator(restOrientation);
        final double angleZ = rotatorZ.dotProduct(Vector3.Z_VECTOR);

        final Orientation thisOrientationRotatedInZ = orientation.rotate(rotatorZ);
        final Vector3 rotatorY = thisOrientationRotatedInZ.noseVector.findRotator(restOrientation);
        final double angleY = rotatorY.dotProduct(Vector3.Y_VECTOR);

        final Orientation thisOrientationRotatedInZy = thisOrientationRotatedInZ.rotate(rotatorY);
        final Vector3 rotatorX = thisOrientationRotatedInZy.roofVector.findRotator(Vector3.Z_VECTOR);
        final double angleX = rotatorX.dotProduct(Vector3.X_VECTOR.scaled(-1));

        return new ZyxOrientedPosition(position, new Vector3(-angleX, -angleY, -angleZ));
    }

    public OrientedPosition toGlobalPosition(final OrientedPosition globalOrigin) {
        final Vector3 angularDisplacement = globalOrigin.orientation.asAngularDisplacement().scaled(1, 1, 1);

        final Vector3 rotatedTranslatedPosition = position.rotate(angularDisplacement).plus(globalOrigin.position);
        final Orientation rotatedOrientation = orientation.rotate(angularDisplacement);

        return new OrientedPosition(rotatedTranslatedPosition, rotatedOrientation);
    }
}
