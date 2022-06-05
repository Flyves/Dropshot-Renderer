package util.math.vector;

import rlbotexample.dynamic_objects.car.orientation.Orientation;

import java.io.Serializable;

public class ZyxOrientedPosition implements Serializable {
    public Vector3 position;
    public Vector3 eulerZYX;

    public ZyxOrientedPosition(Vector3 position, Vector3 eulerZYX) {
        this.position = position;
        this.eulerZYX = eulerZYX;
    }

    public OrientedPosition toCarOrientedPosition() {
        Vector3 rotatorZ = Vector3.Z_VECTOR.scaled(eulerZYX.z);
        Vector3 rotatorY = Vector3.Y_VECTOR.rotate(rotatorZ).scaled(eulerZYX.y);
        Vector3 rotatorX = Vector3.X_VECTOR.scaled(-1).rotate(rotatorZ).rotate(rotatorY).scaled(eulerZYX.x);

        Vector3 nose = Vector3.X_VECTOR
                .rotate(rotatorZ)
                .rotate(rotatorY);
        Vector3 roof = Vector3.Z_VECTOR
                .rotate(rotatorY)
                .rotate(rotatorX);

        return new OrientedPosition(position, new Orientation(nose, roof));
    }
}
