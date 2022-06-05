package util.discrete_functions;

import util.math.vector.Vector3;

public class ExponentialSmoother3D {

    private final double convergenceRate;
    private Vector3 smoothedPosition;

    public ExponentialSmoother3D(double convergenceRate) {
        this.convergenceRate = convergenceRate;
        this.smoothedPosition = new Vector3();
    }

    public Vector3 apply(Vector3 destination) {
        smoothedPosition = smoothedPosition.scaled(convergenceRate)
                .plus(destination.scaled(1-convergenceRate));

        return smoothedPosition;
    }
}
