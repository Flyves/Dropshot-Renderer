package util.shapes;

import util.math.vector.*;

public class Circle3D {

    public final Ray3 center;
    public final double radii;

    private final Circle flatCircle;
    private final double height;
    private final Vector3 orientation;

    public Circle3D(Ray3 center, double radii) {
        this.center = center;
        this.radii = radii;

        this.flatCircle = new Circle(center.offset.flatten(), radii);
        this.height = center.offset.z;
        this.orientation = center.direction;
    }

    public Circle3D scaled(double factor) {
        return new Circle3D(center, radii*factor);
    }

    public Vector3 findPointOnCircle(double rads) {
        final Vector3 localPointOnFlatCircle = new Vector3(flatCircle.findPointOnCircle(rads).minus(flatCircle.center), 0);

        final double angleOfRotation = orientation.angle(Vector3.Z_VECTOR);
        final Vector3 rotationVector = Vector3.Z_VECTOR.crossProduct(orientation).scaledToMagnitude(angleOfRotation);
        final Vector3 localPointOn3DCircle = localPointOnFlatCircle.rotate(rotationVector);
        final Vector3 circleOffset = new Vector3(flatCircle.center, height);

        return localPointOn3DCircle.plus(circleOffset);
    }

    public Vector3 findClosestPointFrom(Vector3 v) {
        final Vector3 centerOffset = new Vector3(flatCircle.center, height);
        final Vector3 localV = v.minus(centerOffset);
        final Vector3 localVProjectedOnLocalCirclePlane = localV.minus(localV.projectOnto(orientation));

        final Vector3 localResult = localVProjectedOnLocalCirclePlane.scaledToMagnitude(flatCircle.radii);

        return localResult.plus(centerOffset);
    }

    // this function does not check for intersections.
    // if you try to pass an intersecting plane as a parameter, it will still compute
    // the point on the circle that is most oriented towards the plane
    public Vector3 findClosestPointFromNonIntersecting(Plane3D plane) {
        Vector3 projection = center.offset.projectOnto(plane);
        return findClosestPointFrom(projection);
    }

    public double findRadsFromClosestPoint(Vector3 v) {
        final Vector3 centerOffset = new Vector3(flatCircle.center, height);
        final Vector3 closestPoint = findClosestPointFrom(v).minus(centerOffset);
        final double angleOfRotation = orientation.angle(Vector3.Z_VECTOR);
        final Vector3 rotationVector = Vector3.Z_VECTOR.crossProduct(orientation).scaledToMagnitude(-angleOfRotation);
        final Vector3 flattenedPointOnCircle = closestPoint.rotate(rotationVector);
        return Math.atan2(flattenedPointOnCircle.y, flattenedPointOnCircle.x);
    }

    public Ray3 findTangentPointFrom(Vector3 point, int tangentId) {
        /*
        if(point.y >= center.offset.y) {
            tangentId = 1-tangentId;
        }*/

        Vector3 offset = new Vector3(flatCircle.center, height);
        Vector3 notRotatedLocalPoint = point.minus(offset);
        Vector3 rotator = orientation.findRotator(Vector3.Z_VECTOR);

        Vector3 rotatedLocalPoint = notRotatedLocalPoint.rotate(rotator);
        Vector2 flatPoint = rotatedLocalPoint.flatten();

        Circle centeredCircle = new Circle(new Vector2(), flatCircle.radii);

        Ray2 tangent1 = centeredCircle.findTangentFrom(flatPoint, tangentId);
        Ray2 tangent2 = centeredCircle.findTangentFrom(flatPoint, 1-tangentId);
        Ray2 tangent = tangent1;
        if(((tangentId * 2) -1) * tangent1.offset.minus(flatPoint).correctionAngle(tangent2.offset.minus(flatPoint)) < 0) {
            tangent = tangent2;
        }

        Vector3 tangentPoint = new Vector3(tangent.offset, 0).rotate(rotator.scaled(-1)).plus(offset);
        return new Ray3(
                tangentPoint,
                point.minus(tangentPoint));
    }
}
