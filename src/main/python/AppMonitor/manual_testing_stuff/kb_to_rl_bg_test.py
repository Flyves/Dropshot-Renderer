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
from time import sleep

import win32api
import win32con
import win32gui
import win32ui


def main():
    hwnd = win32gui.FindWindow(None, 'Rocket League (64-bit, DX11, Cooked)')
    win32api.SendMessage(hwnd, win32con.WM_KEYDOWN, 0x4A, 0)
    win32api.SendMessage(hwnd, win32con.WM_KEYUP, 0x4A, 0)

    win32api.SendMessage(hwnd, win32con.WM_KEYDOWN, 0x4B, 0)
    win32api.SendMessage(hwnd, win32con.WM_KEYUP, 0x4B, 0)

    win32api.SendMessage(hwnd, win32con.WM_KEYDOWN, 0x48, 0)
    win32api.SendMessage(hwnd, win32con.WM_KEYUP, 0x48, 0)

    win32api.SendMessage(hwnd, win32con.WM_KEYDOWN, 0x48, 0)
    win32api.SendMessage(hwnd, win32con.WM_KEYUP, 0x48, 0)


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    main()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
