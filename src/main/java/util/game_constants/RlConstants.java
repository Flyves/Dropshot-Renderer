package util.game_constants;

import util.math.vector.Vector3;

public class RlConstants {

    public static final double BALL_MASS = 30;
    public static final double BALL_INERTIA = 0.4 * BALL_MASS * RlConstants.BALL_RADIUS * RlConstants.BALL_RADIUS;
    public static final double BALL_BOUNCE_RESTITUTION = 0.6;

    public static final double BALL_RADIUS = 92.75;
    public static final double BALL_MAX_SPEED = 4000;
    public static final double BALL_MAX_SPIN = 2*Math.PI;
    //public static final double BALL_AIR_DRAG_COEFFICIENT = 0.0305;
    public static final double BALL_AIR_DRAG_COEFFICIENT = 0.03;
    public static final double BALL_FAST_SLIDING_FRICTION_FORCE = 230;
    public static final double BALL_SLOW_SLIDING_FRICTION_COEFFICIENT = 0.978;
    public static final double BALL_SPEED_THRESHOLD_TO_APPLY_SLIDING_FRICTION = 230;
    public static final double BALL_MINIMUM_ROLLING_SPEED = 40;
    public static final double BALL_CRITICAL_AMOUNT_OF_TIME_OF_SLOW_SPEED_ROLLING_BEFORE_COMPLETE_STOP = 2.5;
    public static final double BALL_MINIMUM_RPM_WHEN_ROLLING_BEFORE_COMPLETE_STOP = 10;

    public static final double CAR_MAX_SPEED = 2300;
    public static final double CAR_MAX_ANGULAR_ACCELERATION_YAW = 9.10838909318708;
    public static final double CAR_MAX_BOOST_AMOUNT = 100;
    public static final double CAR_MASS = 180;
    public static final double OCTANE_ROOF_ELEVATION_WHEN_DRIVING = 55.934654;
    public static final double OCTANE_POSITION_ELEVATION_WHEN_DRIVING = 17.01;

    public static final double ACCELERATION_DUE_TO_BOOST_IN_AIR = 1057.0;
    public static final double ACCELERATION_DUE_TO_BOOST_ON_GROUND = 991.0;
    public static final double ACCELERATION_DUE_TO_JUMP = 291.667;

    public static final double BOOST_CONSUMPTION_RATE = 33.3333333333;
    public static final double DELTA_V_PER_BOOST_IN_AIR = ACCELERATION_DUE_TO_BOOST_IN_AIR/BOOST_CONSUMPTION_RATE;
    public static final double DELTA_V_PER_BOOST_ON_GROUND = ACCELERATION_DUE_TO_BOOST_ON_GROUND/BOOST_CONSUMPTION_RATE;
    public static double boostToDeltaV(final double boostAmount, boolean isOnGround) {
        return isOnGround ? boostAmount*DELTA_V_PER_BOOST_ON_GROUND : boostAmount*DELTA_V_PER_BOOST_IN_AIR;
    }

    public static final double NORMAL_GRAVITY_STRENGTH = 650;
    public static final Vector3 GRAVITY_VECTOR = Vector3.MINUS_Z_VECTOR.scaled(NORMAL_GRAVITY_STRENGTH);

    public static final double CEILING_HEIGHT = 2044;
    public static final double WALL_DISTANCE_X = 4096;
    public static final double WALL_DISTANCE_Y = 5120;
    public static final double GOAL_SIZE_X = 1786;
    public static final double GOAL_SIZE_Z = 642.775;

    public static final double AMOUNT_OF_TIME_BEFORE_LOSING_SECOND_JUMP = 1.25;

    public static final double BOT_REFRESH_RATE = 120;
    public static final double BOT_REFRESH_TIME_PERIOD = 1/BOT_REFRESH_RATE;
}
