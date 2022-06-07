
class CommunicationData:
    def __init__(self, host, port):
        self.received_sync_data = None
        self.server_host = host
        self.server_port = port

    def receiveSyncData(self, data: str):
        self.received_sync_data = data

    # noinspection PyStatementEffect
    @staticmethod
    def validateCommunicationData(communication_data_cfg):
        try:
            communication_data_cfg.received_sync_data
            communication_data_cfg.server_host
            communication_data_cfg.server_port
            return communication_data_cfg
        except Exception:
            return None

    @staticmethod
    def is_config_file_invalid(communication_data_from_cfg):
        return communication_data_from_cfg is None


class CommunicationDataBuilder:
    def __init__(self):
        self.__host = None
        self.__port = None

    def withHost(self, host):
        self.__host = host
        return self

    def withPort(self, port):
        self.__port = port
        return self

    def build(self):
        return CommunicationData(self.__host, self.__port)
