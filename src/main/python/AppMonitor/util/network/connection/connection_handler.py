from util.logging.logging import Logger
from util.network.connection.connection_handler_states import TryConnectingWithConfigFile
from util.state_machine.state_machine import StateMachine

import win32gui
import win32ui
from ctypes import windll
from PIL import Image

from util.video.frame_extractor import extract_frame


class ServerHandler:
    def __init__(self, logger: Logger):
        self.has_to_restart = False
        self.server_state_machine = StateMachine(TryConnectingWithConfigFile(logger))
        self.is_connected = False
        self.has_received_data = False
        self.server = None
        self.last_received_data = ""
        self.scheduled_indexes = []

    def update(self):
        return self.server_state_machine.exec(self)

    def receive_data(self, str_data: str):
        self.last_received_data = str_data
        self.has_received_data = True
        if str_data == "request-next-frame":
            print("Processing image...")

            # FYI
            # frame_data[0] = pixels
            # frame_data[1] = (subframe index, image index)
            frame_data = self.compute_next_frame()

            print("# " + str(frame_data[1]))

            self.send_frame_data(frame_data[0])
            self.scheduled_indexes.append(frame_data[1])
            print("done.")
        elif str_data == "take-screenshot":
            print("Taking screenshot...")
            self.take_screenshot()
        self.has_received_data = False

    def compute_next_frame(self):
        return extract_frame(self.scheduled_indexes)

    def send_frame_data(self, frame_data):
        self.server.broadcast(frame_data)

    # inspired by this SO thread:
    # https://stackoverflow.com/questions/19695214/screenshot-of-inactive-window-printwindow-win32gui
    # small modifications were made
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
            frame_index = self.scheduled_indexes.pop(0)
            im.save("video_data/" + str(frame_index[0]) + "." + str(frame_index[1]) + ".png")
            print("done.")
        else:
            print("failed.")
