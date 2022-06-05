package util.state_machine;

import rlbotexample.dynamic_objects.DataPacket;

public interface Stopable {
    void stop(DataPacket input);
}
