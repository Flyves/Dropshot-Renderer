package util.math.linear_transform;

public class LinearNormalizer {

    private double lowerBound;
    private double upperBound;

    public boolean isBounded;

    public LinearNormalizer(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.isBounded = false;
    }

    public double compute(double x) {
        double t = (x - lowerBound) / (upperBound - lowerBound);

        if(isBounded) {
            if(t < 0) {
                t = 0;
            }
            else if(t > 1) {
                t = 1;
            }
        }

        return t;
    }

    public double inverse(double t) {
        if(isBounded) {
            if(t < 0) {
                t = 0;
            }
            else if(t > 1) {
                t = 1;
            }
        }

        return (t * (upperBound - lowerBound)) + lowerBound;
    }
}
