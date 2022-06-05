package util.state_machine;

import rlbot.render.Renderer;
import rlbotexample.dynamic_objects.DataPacket;

public interface Debuggable {
    void debug(DataPacket input);
}
