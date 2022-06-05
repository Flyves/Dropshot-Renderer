package util.shapes;

import util.math.vector.Ray3;

public class Plane3D {

    public final Ray3 normal;

    public Plane3D(Ray3 normal) {
        this.normal = normal;
    }
}
