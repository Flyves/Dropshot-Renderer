import socket
from typing import Callable


class Server:
    """
    Use this Server class to easily listen to connections on a specific host and port.
    """
    def __init__(self):
        self.server = socket.socket()
        self.connections = []
        self.reception_callback = None

    def start(self, host='', port=5000):
        self.server.bind((host, port))
        self.server.listen(1)
        received_socket, address = self.server.accept()
        self.connections.append(received_socket)
        print("received connection")

        while True:
            try:
                self.reception_callback(received_socket.recv(100).decode())
            except Exception:
                self.connections.clear()
                break
        received_socket.close()
        self.server.close()

    def stop(self):
        self.server.close()

    def set_reception_callback(self, callback: Callable[[str], None]):
        self.reception_callback = callback

    def broadcast(self, str_data: str):
        for sock in self.connections:
            try:
                sock.sendall(str_data.encode())
            except Exception:
                sock.close()
                self.connections.clear()

    def amount_of_connections(self):
        return len(self.connections)
