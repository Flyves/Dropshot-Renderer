from util.logging.logging import Logger
from util.network.connection.connection_handler_states import TryConnectingWithConfigFile
from util.state_machine.state_machine import StateMachine

import win32gui
import win32ui
from ctypes import windll
from PIL import Image

from util.video.frame_extractor import retrieve_next_frame_index, extract_frame


class ServerHandler:
    def __init__(self, logger: Logger):
        self.server_state_machine = StateMachine(TryConnectingWithConfigFile(logger))
        self.is_connected = False
        self.has_received_data = False
        self.server = None
        self.last_received_data = ""

    def update(self):
        return self.server_state_machine.exec(self)

    def receive_data(self, str_data: str):
        self.last_received_data = str_data
        self.has_received_data = True
        if str_data == "request-current-frame":
            print("processing image...")
            frame_data = self.compute_current_frame()
            self.send_frame_data(frame_data)
            print("done.")
        if str_data == "request-next-frame":
            print("processing image...")
            frame_data = self.compute_next_frame()
            self.send_frame_data(frame_data)
            print("done.")
        elif str_data == "take-screenshot":
            print("taking_screenshot...")
            self.take_screenshot()
        self.has_received_data = False

    def compute_current_frame(self):
        return extract_frame()

    def compute_next_frame(self):
        return extract_frame(1)

    def send_frame_data(self, frame_data):
        self.server.broadcast(frame_data)

    # inspired by this SO thread:
    # https://stackoverflow.com/questions/19695214/screenshot-of-inactive-window-printwindow-win32gui
    # just some small modifications were made
    def take_screenshot(self):
        hwnd = win32gui.FindWindow(None, 'Rocket League (64-bit, DX11, Cooked)')
        left, top, right, bot = win32gui.GetClientRect(hwnd)
        w = right - left
        h = bot - top

        hwndDC = win32gui.GetWindowDC(hwnd)
        mfcDC = win32ui.CreateDCFromHandle(hwndDC)
        saveDC = mfcDC.CreateCompatibleDC()

        saveBitMap = win32ui.CreateBitmap()
        saveBitMap.CreateCompatibleBitmap(mfcDC, w, h)
        saveDC.SelectObject(saveBitMap)

        result = windll.user32.PrintWindow(hwnd, saveDC.GetSafeHdc(), 1)

        bmpinfo = saveBitMap.GetInfo()
        bmpstr = saveBitMap.GetBitmapBits(True)

        im = Image.frombuffer(
            'RGB',
            (bmpinfo['bmWidth'], bmpinfo['bmHeight']),
            bmpstr, 'raw', 'BGRX', 0, 1)

        win32gui.DeleteObject(saveBitMap.GetHandle())
        saveDC.DeleteDC()
        mfcDC.DeleteDC()
        win32gui.ReleaseDC(hwnd, hwndDC)

        if result == 1:
            im.save("video_data/" + str(retrieve_next_frame_index()) + ".png")
            print("done.")
        else:
            print("failed.")
