package util.math.vector;

import java.io.Serializable;

public class Quaternion implements Serializable {
    public double r;
    public Vector3 v;

    public Quaternion(double r, double i, double j, double k) {
        this.r = r;
        this.v = new Vector3(i, j, k);
    }

    public Quaternion(double r, Vector3 v) {
        this.r = r;
        this.v = v;
    }

    public Quaternion multiply(Quaternion other) {
        return new Quaternion(
                r*other.r - v.x*other.v.x - v.y*other.v.y - v.z*other.v.z,
                r*other.v.x + v.x*other.r + v.y*other.v.z - v.z*other.v.y,
                r*other.v.y + v.y*other.r + v.z*other.v.x - v.x*other.v.z,
                r*other.v.z + v.z*other.r + v.x*other.v.y - v.y*other.v.x);
    }

    public Quaternion mutableMultiply(Quaternion other) {
        r = r*other.r - v.x*other.v.x - v.y*other.v.y - v.z*other.v.z;
        v.x = r*other.v.x + v.x*other.r + v.y*other.v.z - v.z*other.v.y;
        v.y = r*other.v.y + v.y*other.r + v.z*other.v.x - v.x*other.v.z;
        v.z = r*other.v.z + v.z*other.r + v.x*other.v.y - v.y*other.v.x;
        return this;
    }

    public Quaternion conjugate() {
        return new Quaternion(r, -v.x, -v.y, -v.z);
    }

    public Quaternion mutableConjugate() {
        v.x = -v.x;
        v.y = -v.y;
        v.z = -v.z;
        return this;
    }

    // e^this
    public Quaternion exp() {
        double eP = Math.exp(this.r);
        double vMag = v.magnitude();

        double resultReal = eP * Math.cos(vMag);
        Vector3 resultIm = v.scaledToMagnitude(eP * Math.sin(vMag));

        return new Quaternion(resultReal, resultIm);
    }

    public Vector3 toVector3() {
        return new Vector3(v.x, v.y, v.z);
    }

    public Vector3 mutableToVector3() {
        return v;
    }

    @Override
    public String toString() {
        return "[" + r + ", " + v.x + ", " + v.y + ", " + v.z + "]";
    }
}
