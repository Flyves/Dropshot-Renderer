import tkinter as tk
from tkinter import messagebox


class ConnectionGUI:
    def __init__(self):
        self.tk = tk.Tk()
        self.server_host = None
        self.server_port = None
        self.has_requested_connection = False
        tk.Label(text="Server host").grid(row=1, column=0)
        tk.Label(text="port").grid(row=1, column=2)
        self.__server_host_entry = tk.Entry(self.tk)
        self.__server_port_entry = tk.Entry(self.tk, textvariable=tk.StringVar(self.tk, '5000'))
        self.__server_host_entry.grid(row=1, column=1)
        self.__server_port_entry.grid(row=1, column=3)
        tk.Button(self.tk, text="Connect", command=self.__try_parsing_entries).grid(row=4, column=0)

    def update(self):
        self.tk.update()

    def quit(self):
        self.tk.destroy()

    def __try_parsing_entries(self):
        self.server_host = self.__server_host_entry.get()
        self.server_port = self.__server_port_entry.get()
        try:
            self.server_port = int(self.server_port)
            if not (0 <= self.server_port < 65536):
                raise ValueError()
        except ValueError:
            messagebox.showerror("Bad port value", "The port must be an integer between 0 and 65535!")
            return None
        self.has_requested_connection = True

