package util.state_machine;

import rlbotexample.dynamic_objects.DataPacket;

public class StateMachine implements Behaviour, Debuggable {
    private State state;
    private State nextState;

    public StateMachine(State initState) {
        state = null;
        nextState = initState;
    }

    public void exec(DataPacket input) {
        if(nextState != state) {
            nextState.start(input);
        }

        state = nextState;
        state.exec(input);
        nextState = state.next(input);

        if(nextState != state) {
            state.stop(input);
        }
    }

    public void debug(DataPacket input) {
        state.debug(input);
    }
}
