package rlbotexample.dynamic_objects.car;

import util.math.orientation.Orientation;
import util.math.vector.Vector3;
import util.shapes.HitBox;

public class CarData {

    public final Vector3 position;
    public final Vector3 velocity;
    public final Vector3 spin;
    public final double boost;
    public final double elapsedSeconds;
    public final HitBox hitBox;

    public CarData(rlbot.flat.PlayerInfo playerInfo, float elapsedSeconds) {
        this.position = new Vector3(playerInfo.physics().location());
        this.velocity = new Vector3(playerInfo.physics().velocity());
        this.spin = new Vector3(playerInfo.physics().angularVelocity());
        this.boost = playerInfo.boost();
        final Orientation orientation = Orientation.fromFlatbuffer(playerInfo);
        this.elapsedSeconds = elapsedSeconds;
        this.hitBox = new HitBox(position, playerInfo.hitboxOffset(), playerInfo.hitbox(), orientation.noseVector, orientation.roofVector);
    }
}
