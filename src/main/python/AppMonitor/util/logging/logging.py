from abc import abstractmethod

from overrides import overrides


class Logger:
    @abstractmethod
    def log(self, *args):
        pass


class ConsoleLogger(Logger):
    @overrides
    def log(self, *args):
        print(*args)
