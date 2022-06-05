package util.math.linear_transform;

import util.math.vector.Vector3;

public class ParameterizedSegment {

    private final Vector3 zero;
    private final Vector3 one;

    public ParameterizedSegment(Vector3 zero, Vector3 one) {
        this.zero = zero;
        this.one = one;
    }

    public Vector3 compute(double t) {
        return zero.scaled(1-t).plus(one.scaled(t));
    }
}
