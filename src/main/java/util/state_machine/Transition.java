package util.state_machine;

import rlbotexample.dynamic_objects.DataPacket;

public interface Transition {
    State next(DataPacket input);
}
