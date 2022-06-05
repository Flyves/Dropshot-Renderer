from typing import TypeVar, Generic

from overrides import overrides

T = TypeVar('T')


class State(Generic[T]):
    def start(self, param: T):
        pass

    def stop(self, param: T):
        pass

    def exec(self, param: T):
        pass

    def next(self, param: T):
        return self


class StateMachine(State[T]):
    def __init__(self, init_state):
        self.state = None
        self.nextState = init_state

    @overrides
    def exec(self, param: T):
        if self.nextState is not self.state:
            self.nextState.start(param)
        self.state = self.nextState
        output = self.state.exec(param)
        self.nextState = self.state.next(param)
        if self.nextState is not self.state:
            self.state.stop(param)

        return output
