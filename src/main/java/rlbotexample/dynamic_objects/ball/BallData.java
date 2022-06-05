package rlbotexample.dynamic_objects.ball;


import rlbot.flat.BallInfo;
import rlbot.flat.Physics;
import util.game_constants.RlConstants;
import util.math.vector.Vector3;

/**
 * Basic information about the getNativeBallPrediction.
 *
 * This class is here for your convenience, it is NOT part of the framework. You can change it as much
 * as you want, or delete it.
 */
public class BallData {
    public final Vector3 position;
    public final Vector3 velocity;
    public final Vector3 spin;
    public final double time;

    public BallData(final BallInfo ball) {
        this.position = new Vector3(ball.physics().location());
        this.velocity = new Vector3(ball.physics().velocity());
        this.spin = new Vector3(ball.physics().angularVelocity());
        this.time = 0;
    }
}
