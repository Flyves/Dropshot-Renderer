package util.shapes;

import rlbot.flat.BoxShape;
import rlbotexample.dynamic_objects.car.orientation.Orientation;
import util.math.vector.Vector3;

public class HitBox {
    public final Vector3 centerPositionOfHitBox;
    public final Vector3 localHitBoxOffset;
    public final Vector3 cornerPosition;
    public final Vector3 frontOrientation;
    public final Vector3 roofOrientation;

    public HitBox(Vector3 centerPositionOfCar, rlbot.flat.Vector3 centerOfMassOffset, BoxShape boxShape, Vector3 frontOrientation, Vector3 roofOrientation) {
        this.centerPositionOfHitBox = centerPositionOfCar.plus(new Vector3(centerOfMassOffset).scaled(-1, 1, 1).matrixRotation(frontOrientation, roofOrientation));
        this.localHitBoxOffset = new Vector3(centerOfMassOffset).scaled(-1, 1, 1);
        this.cornerPosition = new Vector3(boxShape.length(), boxShape.width(), boxShape.height()).scaled(0.5);
        this.frontOrientation = frontOrientation;
        this.roofOrientation = roofOrientation;
    }

    private HitBox(Vector3 centerPosition, Vector3 boxSize, Orientation orientation) {
        this.centerPositionOfHitBox = centerPosition;
        // this one is simply wrong LOL
        // we need to infer the hit box offset
        // with static variables that we set at
        // the start of the program...
        this.localHitBoxOffset = null;
        this.cornerPosition = boxSize;
        this.frontOrientation = orientation.noseVector;
        this.roofOrientation = orientation.roofVector;
    }

    // doesn't work if the point is withing the hit box, but we hardly need that
    public Vector3 closestPointOnSurface(Vector3 globalPoint) {
        Vector3 localPoint = getLocal(globalPoint);

        double newXCoordinate = localPoint.x;
        if(localPoint.x > cornerPosition.x) {
            newXCoordinate = cornerPosition.x;
        }
        else if(localPoint.x < -cornerPosition.x) {
            newXCoordinate = -cornerPosition.x;
        }

        double newYCoordinate = -localPoint.y;
        if(localPoint.y > cornerPosition.y) {
            newYCoordinate = -cornerPosition.y;
        }
        else if(localPoint.y < -cornerPosition.y) {
            newYCoordinate = cornerPosition.y;
        }

        double newZCoordinate = localPoint.z;
        if(localPoint.z > cornerPosition.z) {
            newZCoordinate = cornerPosition.z;
        }
        else if(localPoint.z < -cornerPosition.z) {
            newZCoordinate = -cornerPosition.z;
        }

        return getGlobal(new Vector3(newXCoordinate, newYCoordinate, newZCoordinate));
    }

    private double makeNonZero(double value) {
        if(Math.abs(value) < 0.00001) {
            return 0.00001;
        }
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HitBox)) {
            return false;
        }
        return ((HitBox)obj).centerPositionOfHitBox.minus(this.centerPositionOfHitBox).magnitudeSquared() < 0.1
                && ((HitBox)obj).cornerPosition.minus(this.cornerPosition).magnitudeSquared() < 0.1
                && ((HitBox)obj).frontOrientation.minus(this.frontOrientation).magnitudeSquared() < 0.1
                && ((HitBox)obj).roofOrientation.minus(this.roofOrientation).magnitudeSquared() < 0.1;
    }

    @Override
    public int hashCode() {
        return centerPositionOfHitBox.hashCode()
                + cornerPosition.hashCode()
                + frontOrientation.hashCode()
                + roofOrientation.hashCode();
    }

    private Vector3 getLocal(Vector3 globalPoint) {
        return globalPoint.minus(centerPositionOfHitBox).toFrameOfReference(frontOrientation, roofOrientation);
    }

    private Vector3 getGlobal(Vector3 localPoint) {
        return localPoint.matrixRotation(frontOrientation, roofOrientation).plus(centerPositionOfHitBox);
    }

    public boolean isCollidingWith(HitBox that) {
        Vector3 pointOnThis = this.closestPointOnSurface(that.centerPositionOfHitBox);
        Vector3 pointOnThat = that.closestPointOnSurface(this.centerPositionOfHitBox);
        return this.centerPositionOfHitBox.minus(that.centerPositionOfHitBox).magnitudeSquared() <
                square((that.centerPositionOfHitBox.minus(pointOnThat).magnitude()
                        + this.centerPositionOfHitBox.minus(pointOnThis).magnitude())*1.2);
    }

    private double square(double x) {
        return x*x;
    }
}
