package util.math.vector;

import com.google.flatbuffers.FlatBufferBuilder;
import rlbot.flat.Rotator;
import rlbot.gamestate.DesiredRotation;
import rlbot.gamestate.DesiredVector3;
import util.math.orientation.Orientation;
import util.math.matrix.Matrix3By3;
import util.shapes.Plane3D;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple 3d vector class with the most essential operations.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can add to it as much
 * as you want, or delete it.
 */
public class Vector3 implements Serializable {

    public static final Vector3 Z_VECTOR = new Vector3(0, 0, 1);
    public static final Vector3 MINUS_Z_VECTOR = new Vector3(0, 0, -1);
    public static final Vector3 X_VECTOR = new Vector3(1, 0, 0);
    public static final Vector3 Y_VECTOR = new Vector3(0, 1, 0);

    public double x;
    public double y;
    public double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector2 xy, double z) { this(xy.x, xy.y, z); }

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3(Vector3 v) {
        this(v.x, v.y, v.z);
    }

    public Vector3(Rotator rotator) {
        this(rotator.pitch(), rotator.yaw(), rotator.roll());
    }

    public Vector3(rlbot.flat.Vector3 vec) {
        // Invert the X value so that the axes make more sense.
        this(-vec.x(), vec.y(), vec.z());
    }

    public Vector3(DesiredVector3 location) {
        this(location.getX(), location.getY(), location.getZ());
    }

    public static Vector3 generateRandomVector() {
        return new Vector3(Math.random()*2-1, Math.random()*2-1, Math.random()*2-1);
    }

    public int toFlatbuffer(FlatBufferBuilder builder) {
        // Invert the X value again so that rlbot sees the format it expects.
        return rlbot.flat.Vector3.createVector3(builder, (float)-x, (float)y, (float)z);
    }

    public Vector3 plus(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 minus(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    public Vector3 scaled(double scale) {
        return new Vector3(x * scale, y * scale, z * scale);
    }

    public Vector3 scaled(Vector3 scale) {
        return new Vector3(x * scale.x, y * scale.y, z * scale.z);
    }

    public Vector3 scaled(double scaleX, double scaleY, double scaleZ) {
        return new Vector3(x * scaleX, y * scaleY, z * scaleZ);
    }
    public Vector3 scaled(Matrix3By3 scale) {
        return new Vector3(
                x*scale.a1.x + y*scale.a2.x + z*scale.a3.x,
                x*scale.a1.y + y*scale.a2.y + z*scale.a3.y,
                x*scale.a1.z + y*scale.a2.z + z*scale.a3.z);
    }

    public Vector3 mutableScaled(double scale) {
        x = x * scale;
        y = y * scale;
        z = z * scale;
        return this;
    }

    /**
     * If magnitude is negative, we will return a vector facing the opposite direction.
     */
    public Vector3 scaledToMagnitude(double magnitude) {
        if (isZero()) {
            return new Vector3();
        }
        double scaleRequired = magnitude / magnitude();
        return scaled(scaleRequired);
    }
    public Vector3 scaledToMagnitude(double magnitudeX, double magnitudeY, double magnitudeZ) {
        double scaleRequiredX = magnitudeX / Math.abs(x);
        if (Double.isNaN(scaleRequiredX)) {
            scaleRequiredX = magnitudeX;
        }
        double scaleRequiredY = magnitudeY / Math.abs(y);
        if (Double.isNaN(scaleRequiredY)) {
            scaleRequiredY = magnitudeY;
        }
        double scaleRequiredZ = magnitudeZ / Math.abs(z);
        if (Double.isNaN(scaleRequiredZ)) {
            scaleRequiredZ = magnitudeZ;
        }
        return scaled(scaleRequiredX, scaleRequiredY, scaleRequiredZ);
    }


    public Vector3 mutableScaledToMagnitude(double magnitude) {
        if (isZero()) {
            return this;
        }
        double scaleRequired = magnitude / magnitude();
        return mutableScaled(scaleRequired);
    }

    public double distance(Vector3 other) {
        double xDiff = x - other.x;
        double yDiff = y - other.y;
        double zDiff = z - other.z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }

    public Vector3 inverse() {
        return new Vector3(1/x, 1/y, 1/z);
    }

    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    public double magnitudeSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3 normalized() {

        if (isZero()) {
            return new Vector3();
        }
        return this.scaled(1 / magnitude());
    }

    public Vector3 mutableNormalized() {

        if (isZero()) {
            return this;
        }
        return this.mutableScaled(1 / magnitude());
    }

    public double dotProduct(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public Vector2 flatten() {
        return new Vector2(x, y);
    }

    public double angle(Vector3 v) {
        double mag2 = magnitudeSquared();
        double vmag2 = v.magnitudeSquared();
        final double det = Math.sqrt(mag2 * vmag2);
        if(Math.sqrt(mag2 * vmag2) < 0.000000001) {
            return 0;
        }
        double dot = dotProduct(v);
        return Math.acos(dot / det);
    }

    public Vector3 crossProduct(Vector3 v) {
        double tx = y * v.z - z * v.y;
        double ty = z * v.x - x * v.z;
        double tz = x * v.y - y * v.x;
        return new Vector3(tx, ty, tz);
    }

    public Vector3 mutableCrossProduct(Vector3 v) {
        final double x2 = y * v.z - z * v.y;
        final double y2 = z * v.x - x * v.z;
        final double z2 = x * v.y - y * v.x;
        x = x2;
        y = y2;
        z = z2;
        return this;
    }

    public Vector3 projectOnto(Vector3 vectorToProjectOnto) {
        return vectorToProjectOnto.scaled(this.dotProduct(vectorToProjectOnto)/vectorToProjectOnto.magnitudeSquared());
    }

    public Vector3 projectOnto(Plane3D plane) {
        return this.minus(this.projectOnto(plane.normal.direction))
                .plus(plane.normal.offset.projectOnto(plane.normal.direction));
    }

    /** very wrongly implemented method. The name is definitely not representative of what it's doing. I was still learning about
     * how to tackle rotations when I implemented it. It's basically doing euclidian rotations in X-Z order, but from a weird starting orientation.
     * Don't use it. It's working properly where it's used, but you should definitely use the "rotate" function instead.
     * The "rotate" function is way more intuitive and reliable.
     * @param forwardFacingVector
     * @param roofFacingVector
     * @return stuff rotated in X-Z order, it's weird don't use it.
     */
    @Deprecated
    public Vector3 matrixRotation(Vector3 forwardFacingVector, Vector3 roofFacingVector) {
        Vector3 result = new Vector3(this);

        // roll
        Vector3 rotatedRoll = roofFacingVector.orderedMinusAngle(forwardFacingVector);
        Vector2 rollProjection = new Vector2(rotatedRoll.z, rotatedRoll.y);
        Vector2 rotatedInRollLocalPointProjection = new Vector2(result.z, result.y).minusAngle(rollProjection).plusAngle(new Vector2(0, 1));
        result = new Vector3(result.x, rotatedInRollLocalPointProjection.x, rotatedInRollLocalPointProjection.y);

        // computing global pitch and yaw
        Vector2 pitchProjection = new Vector2(forwardFacingVector.flatten().magnitude(), -forwardFacingVector.z);
        Vector2 localPointProjection = new Vector2(result.x, result.z);
        Vector2 rotatedLocalPointProjection = localPointProjection.minusAngle(pitchProjection);
        result = new Vector3(rotatedLocalPointProjection.x, result.y, rotatedLocalPointProjection.y);
        result = result.orderedPlusAngle(new Vector3(forwardFacingVector.flatten(), 0));

        return result;
    }

    /** very wrongly implemented method. The name is definitely not representative of what it's doing. I was still learning about
     * how to tackle rotations when I implemented it. It's basically doing euclidian rotations in X-Z order, but from a weird starting orientation.
     * Don't use it. It's working properly where it's used, but you should definitely use the "rotate" function instead.
     * The "rotate" function is way more intuitive and reliable.
     * @param orientation
     * @return stuff rotated in X-Z order, it's weird don't use it.
     */
    @Deprecated
    public Vector3 matrixRotation(Orientation orientation) {
        Vector3 result = new Vector3(this);

        // roll
        Vector3 rotatedRoll = orientation.roofVector.orderedMinusAngle(orientation.noseVector);
        Vector2 rollProjection = new Vector2(rotatedRoll.z, rotatedRoll.y);
        Vector2 rotatedInRollLocalPointProjection = new Vector2(result.z, result.y).minusAngle(rollProjection).plusAngle(new Vector2(0, 1));
        result = new Vector3(result.x, rotatedInRollLocalPointProjection.x, rotatedInRollLocalPointProjection.y);

        // computing global pitch and yaw
        Vector2 pitchProjection = new Vector2(orientation.noseVector.flatten().magnitude(), -orientation.noseVector.z);
        Vector2 localPointProjection = new Vector2(result.x, result.z);
        Vector2 rotatedLocalPointProjection = localPointProjection.minusAngle(pitchProjection);
        result = new Vector3(rotatedLocalPointProjection.x, result.y, rotatedLocalPointProjection.y);
        result = result.orderedPlusAngle(new Vector3(orientation.noseVector.flatten(), 0));

        return result;
    }

    /** very wrongly implemented method. The name is definitely not representative of what it's doing. I was still learning about
     * how to tackle rotations when I implemented it. It's basically doing euclidian rotations in Z-X order, but from a weird starting orientation.
     * Don't use it. It's working properly where it's used, but you should definitely use the "rotate" function instead.
     * The "rotate" function is way more intuitive and reliable.
     * @param orientation
     * @return stuff rotated in Z-X order, it's weird don't use it.
     */
    @Deprecated
    public Vector3 toFrameOfReference(Orientation orientation)
    {
        return toFrameOfReference(orientation.noseVector, orientation.roofVector);
    }

    /** very wrongly implemented method. The name is definitely not representative of what it's doing. I was still learning about
     * how to tackle rotations when I implemented it. It's basically doing euclidian rotations in Z-X order, but from a weird starting orientation.
     * Don't use it. It's working properly where it's used, but you should definitely use the "rotate" function instead.
     * The "rotate" function is way more intuitive and reliable.
     * @param frontDirection
     * @param topDirection
     * @return stuff rotated in Z-X order, it's weird don't use it.
     */
    @Deprecated
    public Vector3 toFrameOfReference(Vector3 frontDirection, Vector3 topDirection)
    {
        // Calculate the vector without any roll yet (the roll is calculated from the topDirection vector)
        Vector3 frameOfRefWithoutRoll = this.orderedMinusAngle(frontDirection);
        // Calculate the roll vector in the frame of ref so we can use it to do a planar projection followed by a rotation later on.
        // Basically, the vector is going to do nothing if it faces upward, and it's going to subtract its angle from
        // that top position if it has any
        Vector3 rollInFrameOfRef = topDirection.orderedMinusAngle(frontDirection);

        // Calculating the 2D equivalents in the planar projection
        Vector2 flattenedFrameOfRefWithoutRoll = new Vector2(frameOfRefWithoutRoll.z, frameOfRefWithoutRoll.y);
        Vector2 flattenedRollInFrameOfRef = new Vector2(rollInFrameOfRef.z, rollInFrameOfRef.y);

        // Applying the roll rotation
        Vector2 planarProjectionZyOfResult = flattenedFrameOfRefWithoutRoll.minusAngle(flattenedRollInFrameOfRef);

        // Put back into a coherent form the calculated coordinates and return the vector
        return new Vector3(frameOfRefWithoutRoll.x, planarProjectionZyOfResult.y, planarProjectionZyOfResult.x);
    }

    /** simple rotation of this vector by another using quaternion algebra.
     * @param r a rotation vector
     * @return this vector, rotated by the vector r using the right-hand rule.
     */
    public Vector3 rotate(Vector3 r) {
        final double a = r.magnitude()*0.5;
        final Vector2 r2 = new Vector2(Math.cos(a), Math.sin(a));
        final Vector3 sr = r.scaledToMagnitude(r2.y);
        final Quaternion qr = new Quaternion(r2.x, sr);
        final Quaternion qa = new Quaternion(0, this);
        final Quaternion qr2 = new Quaternion(r2.x, sr.scaled(-1));

        return qr.multiply(qa.multiply(qr2)).toVector3();
    }

    /** Same as "rotate(r)", but it's mutable for the parameter, so it modifies r as a result.
     * @param r a rotation vector. Watch out, r is going to change as a result of calling this function.
     * @return this vector, rotated by the vector r using the right-hand rule.
     */
    public Vector3 mutableParamRotate(final Vector3 r) {
        final double a = r.magnitude()*0.5;
        final Vector3 sr = r.mutableScaledToMagnitude(Math.sin(a));
        final Quaternion qr = new Quaternion(Math.cos(a), sr);
        final Quaternion qv = new Quaternion(0, this);

        return ((qr.multiply(qv)).multiply(qr.mutableConjugate())).mutableToVector3();
    }

    /** Not the right way to do rotations. It's subtracting the angle of a vector from the X axis in 3D to this vector.
     * Use the "rotate" function instead.
     * @param rotationVector
     * @return this vector, rotated by the negative of the angle between rotationVector and the X axis
     */
    @Deprecated
    public Vector3 orderedMinusAngle(Vector3 rotationVector) {
        // Rotating the vector in xy beforehand
        Vector3 firstRotatedVector = new Vector3(this.flatten().minusAngle(rotationVector.flatten()), this.z);
        Vector3 firstRotationVector = new Vector3(rotationVector.flatten().magnitude(), 0, rotationVector.z);

        // Then rotating it in xz (the rotating vector in y is 0, thus we now only need to rotate it in xz)
        Vector2 projectedVectorXz = new Vector2(firstRotatedVector.x, firstRotatedVector.z)
                .minusAngle(new Vector2(firstRotationVector.x, firstRotationVector.z));

        // We can now add the x and the z coordinates separately from the firstly calculated y coordinate
        return new Vector3(projectedVectorXz.x, firstRotatedVector.y, projectedVectorXz.y);
    }


    /** Not the right way to do rotations. It's adding the angle of a vector from the X axis in 3D to this vector.
     * Use the "rotate" function instead.
     * @param rotationVector
     * @return this vector, rotated by the angle between rotationVector and the X axis
     */
    @Deprecated
    public Vector3 orderedPlusAngle(Vector3 rotationVector) {
        // Rotating the vector in xy beforehand
        Vector3 firstRotatedVector = new Vector3(this.flatten().plusAngle(rotationVector.flatten()), this.z);
        Vector3 firstRotationVector = new Vector3(rotationVector.flatten().plusAngle(rotationVector.flatten()), rotationVector.z);

        // Then rotating it in the planar projection of the rotation angle (x, y, 0), (x, y, z)
        Vector2 projectedVectorXyXyz = new Vector2(firstRotatedVector.flatten().magnitude(), firstRotatedVector.z)
                .plusAngle(new Vector2(firstRotationVector.flatten().magnitude(), firstRotationVector.z));

        Vector2 resultXy = firstRotatedVector.flatten().scaledToMagnitude(projectedVectorXyXyz.x);
        // We can now add the x and the z coordinates separately from the firstly calculated y coordinate
        return new Vector3(resultXy.x, resultXy.y, projectedVectorXyXyz.y);
    }

    private double clamp(final double valueToClamp, final double min, final double max) {
        return Math.max(min, Math.min(max, valueToClamp));
    }

    @Override
    public String toString() {
        return "[ x:" + this.x + ", y:" + this.y + ", z:" + this.z + " ]";
    }

    @Override
    public int hashCode() {
        return Objects.hash((int)x, (int)y, (int)z);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Vector3)) {
            return false;
        }
        Vector3 that = (Vector3)o;
        return this.x == that.x
            && this.y == that.y
            && this.z == that.z;
    }

    public DesiredVector3 toDesiredVector3() {
        return new DesiredVector3((float)x, (float)y, (float)z);
    }

    public DesiredVector3 toFlippedDesiredVector3() {
        return new DesiredVector3((float)-x, (float)y, (float)z);
    }

    public rlbot.vector.Vector3 toFlatVector() {
        return new rlbot.vector.Vector3((float)-x, (float)y, (float)z);
    }

    public Vector3 findRotator(Vector3 v) {
        final double angleBetweenVectors = angle(v);
        if(angleBetweenVectors < 0.000001) {
            return new Vector3();
        }
        if(crossProduct(v).magnitudeSquared() < 0.000001) {
            if(v.x != 0 && v.y != 0) {
                return findRotator(new Vector3(0, 0, 1)).scaledToMagnitude(angleBetweenVectors);
            }
        }

        return crossProduct(v).scaledToMagnitude(angleBetweenVectors);
    }

    public Vector3 nonZeroOrElse(Vector3 vector3) {
        if(isZero()) {
            return vector3;
        }
        return this;
    }

    public DesiredRotation toDesiredRotation() {
        return new DesiredRotation((float)y, (float)-z, (float)x);
    }
}
