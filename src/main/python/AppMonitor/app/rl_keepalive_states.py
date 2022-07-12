import os
import subprocess
import time

import win32api
import win32con
import win32gui

from app.process_util import kill_app_with_name, kill_process
from app.window_process_monitor import WindowMonitor
from util.state_machine.state_machine import State

import configparser

config = configparser.ConfigParser()
config.read('userpaths.cfg')

JAVA_BOT_LOCATION = "..\\..\\..\\..\\run-bot.bat"
BAKKESMOD_LOCATION = config['PATHS']['bakkesmod_location']
RLBOT_ROOT_LOCATION = ".\\run.py"
RLBOT_PYTHON_PATH = config['PATHS']['rlbot_python_location']
RLBOT_PYTHON_LAUNCH_SCRIPT = ".\\runpy.bat"

JAVA_BOT_APP_NAME = "Java Bot Handler"
ANY_SHELL_APP_NAME = config['NAMES']['cmd_app_name']
ROCKET_LEAGUE_APP_NAME = config['NAMES']['rocket_league_app_name']
BAKKESMOD_APP_NAME = config['NAMES']['bakkesmod_app_name']

JAVA_SHELL_PROCESS_NAME = "cmd.exe"
RLBOT_PROCESS_NAME = "RLBot.exe"


class CloseEverything(State):
    def start(self, param):
        param.scheduled_indexes.clear()
        print('Restarting...')

    def exec(self, param):
        kill_process(RLBOT_PROCESS_NAME)
        kill_process(JAVA_SHELL_PROCESS_NAME)

        kill_app_with_name(JAVA_BOT_APP_NAME)

        kill_app_with_name(ANY_SHELL_APP_NAME)
        time.sleep(1)
        kill_app_with_name(ANY_SHELL_APP_NAME)
        time.sleep(1)
        kill_app_with_name(ANY_SHELL_APP_NAME)

        kill_app_with_name(ROCKET_LEAGUE_APP_NAME)

        kill_app_with_name(BAKKESMOD_APP_NAME)

    def next(self, param):
        return StartBakkesmod()


class StartBakkesmod(State):
    def exec(self, param):
        os.startfile(BAKKESMOD_LOCATION)
        kill_app_with_name(ROCKET_LEAGUE_APP_NAME)

    def next(self, param):
        return StartJavaServer()


class StartJavaServer(State):
    def exec(self, param):
        os.startfile(JAVA_BOT_LOCATION)
        kill_app_with_name(ROCKET_LEAGUE_APP_NAME)

    def next(self, param):
        return WaitForSteamToSettleDown()


class WaitForSteamToSettleDown(State):
    DELAY = 10

    def __init__(self):
        self.end_time = time.time() + WaitForSteamToSettleDown.DELAY

    def exec(self, param):
        kill_app_with_name(ROCKET_LEAGUE_APP_NAME)

    def next(self, param):
        if self.elapsed():
            return StartRlBotRunPy()
        return self

    def elapsed(self):
        return time.time() >= self.end_time


class StartRlBotRunPy(State):
    def start(self, param):
        print('Launching RLBot')

    def exec(self, param):
        self.overrideRLBotRunScript()
        os.startfile(RLBOT_PYTHON_LAUNCH_SCRIPT)

    def next(self, param):
        return WaitForRocketLeagueToStart()

    def overrideRLBotRunScript(self):
        file = open('runpy.bat', 'w')
        file.write('cd ..\\..\\..\\..\\' + '\n'
                   + RLBOT_PYTHON_PATH + ' ' + RLBOT_ROOT_LOCATION)
        file.close()


class WaitForRocketLeagueToStart(State):
    TIMEOUT_TIME = float(config['TIMEOUTS']['rocket_league_startup'])

    def __init__(self):
        self.end_time = time.time() + WaitForRocketLeagueToStart.TIMEOUT_TIME

    def start(self, param):
        print('Waiting for Rocket League to start...')

    def next(self, param):
        if self.rocketLeagueTimedOut():
            print('Took too long.')
            return CloseEverything()
        if self.rocketLeagueCorrectlyStarted():
            return WaitForClientConnection()
        return self

    def rocketLeagueTimedOut(self):
        return time.time() >= self.end_time

    def rocketLeagueCorrectlyStarted(self):
        return ROCKET_LEAGUE_APP_NAME in WindowMonitor.active_window_names


def rocketLeagueCrashed():
    return ROCKET_LEAGUE_APP_NAME not in WindowMonitor.active_window_names


class WaitForClientConnection(State):
    def start(self, param):
        print('Waiting for bot connection...')

    def next(self, param):
        if rocketLeagueCrashed():
            return CloseEverything()
        if self.client_is_connected(param):
            return RenderingLoop()
        return self

    def client_is_connected(self, param):
        return param.is_connected


class RenderingLoop(State):
    def start(self, param):
        print('Running rendering process.')

        param.has_to_restart = False

        hwnd = win32gui.FindWindow(None, 'Rocket League (64-bit, DX11, Cooked)')
        # set camera angle + position
        win32api.SendMessage(hwnd, win32con.WM_KEYDOWN, 0x4A, 0)
        win32api.SendMessage(hwnd, win32con.WM_KEYUP, 0x4A, 0)

        # set fov
        win32api.SendMessage(hwnd, win32con.WM_KEYDOWN, 0x4B, 0)
        win32api.SendMessage(hwnd, win32con.WM_KEYUP, 0x4B, 0)

        # hide the UI things
        win32api.SendMessage(hwnd, win32con.WM_KEYDOWN, 0x48, 0)
        win32api.SendMessage(hwnd, win32con.WM_KEYUP, 0x48, 0)
        win32api.SendMessage(hwnd, win32con.WM_KEYDOWN, 0x48, 0)
        win32api.SendMessage(hwnd, win32con.WM_KEYUP, 0x48, 0)

    def next(self, param):
        if param.has_to_restart:
            param.has_to_restart = False
            return CloseEverything()
        if rocketLeagueCrashed():
            return CloseEverything()
        return self
