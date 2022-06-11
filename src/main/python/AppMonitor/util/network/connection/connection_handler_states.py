import time
from threading import Thread

import jsonpickle
from overrides import overrides

from util.logging.logging import Logger, ConsoleLogger
from util.network.connection.connection_gui import ConnectionGUI
from util.network.connection.communication_data import CommunicationDataBuilder, CommunicationData
from util.network.server import Server
from util.state_machine.state_machine import State


connection_config_file_path = "app/server_connection.cfg"


def connected(server: Server):
    return server.amount_of_connections() > 0


class TryConnectingWithConfigFile(State):
    def __init__(self, logger: Logger):
        file = open(connection_config_file_path, "r")
        self.communication_data_from_cfg = CommunicationData.validateCommunicationData(jsonpickle.decode(file.read()))
        file.close()
        self.logger = logger

    @overrides
    def next(self, param):
        if CommunicationData.is_config_file_invalid(param.receive_data):
            return AskNetworkInfoGUI(self.logger)
        return ConnectToOtherPlayer(self.communication_data_from_cfg, self.logger)


class AskNetworkInfoGUI(State):
    def __init__(self, logger: Logger):
        self.connection_gui = ConnectionGUI()
        self.logger = logger

    @overrides
    def exec(self, param):
        self.connection_gui.update()

    @overrides
    def stop(self, param):
        self.connection_gui.quit()

    @overrides
    def next(self, param):
        if self.connection_gui.has_requested_connection:
            communication_data = CommunicationDataBuilder()\
                .withHost(self.connection_gui.server_host)\
                .withPort(self.connection_gui.server_port)\
                .build()
            file = open(connection_config_file_path, "w")
            file.write(jsonpickle.encode(communication_data, indent=4))
            file.close()
            return ConnectToOtherPlayer(communication_data, self.logger)
        return self


def cannotConnectToOtherPlayer(thread: Thread):
    return not thread.is_alive()


class ConnectToOtherPlayer(State):
    DELAY_BEFORE_HARD_RESTART = 60*5  # 5 minutes

    def __init__(self, communication_data: CommunicationData, logger: Logger):
        self.server = Server()
        self.communication_data = communication_data
        self.server_connection_thread = None
        self.logger = logger
        self.timeout_time = time.time() + ConnectToOtherPlayer.DELAY_BEFORE_HARD_RESTART

    @overrides
    def start(self, param):
        self.server.set_reception_callback(param.receive_data)
        server_args = (self.communication_data.server_host, self.communication_data.server_port)
        self.server_connection_thread = Thread(target=self.server.start, args=server_args)
        self.server_connection_thread.start()

    def exec(self, param):
        # send hard-restart signal if we're stuck here
        if self.timeout_time > time.time():
            param.hast_to_restart = True

    @overrides
    def next(self, param):
        if connected(self.server):
            return ConnectionEstablished(self.server, self.communication_data, ConsoleLogger())
        if cannotConnectToOtherPlayer(self.server_connection_thread):
            self.server.stop()
            return AskNetworkInfoGUI(self.logger)
        return self


class ConnectionEstablished(State):
    def __init__(self, server: Server, communication_data: CommunicationData, logger: Logger):
        self.server = server
        self.communication_data = communication_data
        self.logger = logger

    @overrides
    def start(self, param):
        param.is_connected = True
        param.server = self.server
        self.logger.log('Connected to client.')
        self.logger.log('server host:', self.communication_data.server_host)
        self.logger.log('server port:', self.communication_data.server_port)

    @overrides
    def stop(self, param):
        self.logger.log('Disconnected from client.')

    @overrides
    def next(self, param):
        if not connected(self.server):
            return TryReconnection(self.server, self.communication_data, self.logger)
        return self


class TryReconnection(State):
    def __init__(self, server: Server, communication_data: CommunicationData, logger: Logger):
        self.server = server
        self.communication_data = communication_data
        self.logger = logger

    @overrides
    def start(self, param):
        param.is_connected = False
        self.logger.log('Trying to reconnect...')
        self.server = Server()
        self.server.set_reception_callback(param.receive_data)
        server_args = (self.communication_data.server_host, self.communication_data.server_port)
        server_connection_thread = Thread(target=self.server.start, args=server_args)
        server_connection_thread.start()
        param.hast_to_restart = True

    @overrides
    def next(self, param):
        if connected(self.server):
            return ConnectionEstablished(self.server, self.communication_data, self.logger)
        return self
