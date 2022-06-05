package util.math.matrix;

import util.math.vector.Vector3;

public class Matrix3By3 {
    public static final Matrix3By3 UNIT = new Matrix3By3(1, 0, 0,
            0, 1, 0,
            0, 0, 1);

    public final Vector3 a1;
    public final Vector3 a2;
    public final Vector3 a3;

    public Matrix3By3(double a11, double a12, double a13,
                      double a21, double a22, double a23,
                      double a31, double a32, double a33) {
        this.a1 = new Vector3(a11, a12, a13);
        this.a2 = new Vector3(a21, a22, a23);
        this.a3 = new Vector3(a31, a32, a33);
    }

    public Matrix3By3(Vector3 a1, Vector3 a2, Vector3 a3) {
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
    }

    public Matrix3By3 scaled(double s) {
        return new Matrix3By3(a1.scaled(s), a2.scaled(s), a3.scaled(s));
    }

    public Matrix3By3 minus(Matrix3By3 that) {
        return new Matrix3By3(
                this.a1.minus(that.a1),
                this.a2.minus(that.a2),
                this.a3.minus(that.a3));
    }

    public Matrix3By3 multiply(Matrix3By3 that) {
        return new Matrix3By3(
                new Vector3(
                        this.a1.x*that.a1.x + this.a1.y*that.a2.x + this.a1.z*that.a3.x,
                        this.a1.x*that.a1.y + this.a1.y*that.a2.y + this.a1.z*that.a3.y,
                        this.a1.x*that.a1.z + this.a1.y*that.a2.z + this.a1.z*that.a3.z),
                new Vector3(
                        this.a2.x*that.a1.x + this.a2.y*that.a2.x + this.a2.z*that.a3.x,
                        this.a2.x*that.a1.y + this.a2.y*that.a2.y + this.a2.z*that.a3.y,
                        this.a2.x*that.a1.z + this.a2.y*that.a2.z + this.a2.z*that.a3.z),
                new Vector3(
                        this.a3.x*that.a1.x + this.a3.y*that.a2.x + this.a3.z*that.a3.x,
                        this.a3.x*that.a1.y + this.a3.y*that.a2.y + this.a3.z*that.a3.y,
                        this.a3.x*that.a1.z + this.a3.y*that.a2.z + this.a3.z*that.a3.z));
    }

    public Vector3 multiply(Vector3 that) {
        return new Vector3(
                a1.dotProduct(that),
                a2.dotProduct(that),
                a3.dotProduct(that));
    }

    public Matrix3By3 inverse() {
        Matrix3By3 adjugate = adjugate();
        return adjugate.scaled(1/determinant());
    }

    public Matrix3By3 adjugate() {
        return cofactor().transpose();
    }

    public Matrix3By3 cofactor() {
        Matrix3By3 minorMatrix = minor();
        return new Matrix3By3(
                minorMatrix.a1.scaled(1, -1, 1),
                minorMatrix.a2.scaled(-1, 1, -1),
                minorMatrix.a3.scaled(1, -1, 1));
    }

    public Matrix3By3 minor() {
        return new Matrix3By3(
                new Vector3(a2.y*a3.z - a2.z*a3.y, a2.x*a3.z - a2.z*a3.x, a2.x*a3.y - a2.y*a3.x),
                new Vector3(a1.y*a3.z - a1.z*a3.y, a1.x*a3.z - a1.z*a3.x, a1.x*a3.y - a1.y*a3.x),
                new Vector3(a1.y*a2.z - a1.z*a2.y, a1.x*a2.z - a1.z*a2.x, a1.x*a2.y - a1.y*a2.x));
    }

    public Matrix3By3 transpose() {
        return new Matrix3By3(
                a1.x, a2.x, a3.x,
                a1.y, a2.y, a3.y,
                a1.z, a2.z, a3.z);
    }

    public double determinant() {
        return    a1.x*(a2.y*a3.z - a2.z*a3.y)
                - a1.y*(a2.x*a3.z - a2.z*a3.x)
                + a1.z*(a2.x*a3.y - a2.y*a3.x);
    }

    // Example code in cpp and python found here:
    // https://learnopencv.com/rotation-matrix-to-euler-angles/
    // Note: this is not a complete code transcription, but just the bear minimum.
    public Vector3 toEulerZyx() {
        Vector3 answer = new Vector3();

        double sy = Math.sqrt(a1.x*a1.x + a2.x*a2.x);
        boolean singular = sy < 1e-6;

        if(!singular) {
            answer.x = Math.atan2(a3.y , a3.z);
            answer.y = Math.atan2(-a3.x, sy);
            answer.z = Math.atan2(a2.x, a1.x);
        }
        else {
            answer.x = Math.atan2(-a2.z, a2.y);
            answer.y = Math.atan2(-a3.x, sy);
        }

        answer = answer.scaled(1, -1, -1);
        answer.z = Math.PI - answer.z;

        return answer;
    }
}