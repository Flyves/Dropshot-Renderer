package util.state_machine;

import rlbotexample.dynamic_objects.DataPacket;

public interface Startable {
    void start(DataPacket input);
}
