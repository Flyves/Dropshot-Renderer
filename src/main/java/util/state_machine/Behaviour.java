package util.state_machine;

import rlbotexample.dynamic_objects.DataPacket;

public interface Behaviour {
    void exec(DataPacket input);
}
