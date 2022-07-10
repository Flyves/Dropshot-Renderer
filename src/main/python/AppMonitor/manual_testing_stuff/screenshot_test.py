#----------------------------------------------------------------------------
# Created By  : John-William Lebel
# Created Date: 2022-06-04
# version = '1.0'
# ---------------------------------------------------------------------------
""" This program is a scheduler for rendering videos on dropshot tiles in rocket league.
    It monitors the status of rocket league and other related apps to make sure everything is running smoothly.

    For example, the rendering process happening in the Java server is very intensive for the game, and so the game
    can crash from time to time.
    If the game crashes, this program will try to restore the crashed game to a state where it can start to render tiles
    again. After launching the game again, the render process restarts where it was left.

    This program also monitors other related programs like bakkesmod and an instance of RlBot to makes sure
    everything is running correctly.

    The state of the rendering process is maintained like this until completion.
"""
# ---------------------------------------------------------------------------
# Imports
# ---------------------------------------------------------------------------

import win32gui
import win32ui
from ctypes import windll
from PIL import Image

# inspired by this SO thread:
# https://stackoverflow.com/questions/19695214/screenshot-of-inactive-window-printwindow-win32gui
# just some small modifications were made
def main():
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
        # PrintWindow Succeeded
        im.save("test.png")


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    main()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
