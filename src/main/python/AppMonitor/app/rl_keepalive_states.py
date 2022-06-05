import os
import time

from app.process_util import kill_app_with_name, kill_process
from app.window_process_monitor import WindowMonitor
from util.state_machine.state_machine import State, StateMachine

JAVA_BOT_LOCATION = "C:\\Users\\Plads\\Documents\\GitHub\\Flyves\\BadAppleDropshot\\run-bot.bat"
BAKKESMOD_LOCATION = "C:\\Program Files\\BakkesMod\\BakkesMod.exe"
RLBOT_LOCATION = ".\\runpy.bat"

JAVA_BOT_APP_NAME = "Java Bot Handler"
ANY_SHELL_APP_NAME = "C:\\Windows\\system32\\cmd.exe"
ROCKET_LEAGUE_APP_NAME = "Rocket League (64-bit, DX11, Cooked)"
BAKKESMOD_APP_NAME = "BakkesModInjectorCpp"

JAVA_SHELL_PROCESS_NAME = "cmd.exe"
RLBOT_PROCESS_NAME = "RLBot.exe"

class CloseEverything(State):

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
    def exec(self, param):
        os.startfile(RLBOT_LOCATION)

    def next(self, param):
        return WaitForRocketLeagueToStart()


class WaitForRocketLeagueToStart(State):
    TIMEOUT_TIME = 60

    def __init__(self):
        self.end_time = time.time() + WaitForRocketLeagueToStart.TIMEOUT_TIME

    def next(self, param):
        if self.rocketLeagueTimedOut():
            return CloseEverything()
        if self.rocketLeagueCorrectlyStarted():
            return RenderingLoop()
        return self

    def rocketLeagueTimedOut(self):
        return time.time() >= self.end_time

    # TODO: change this by "java bot is finally running in game"
    def rocketLeagueCorrectlyStarted(self):
        return ROCKET_LEAGUE_APP_NAME in WindowMonitor.active_window_names


class RenderingLoop(State):
    def exec(self, param):
        print('running rendering process')

    def next(self, param):
        if self.rocketLeagueCrashed():
            return CloseEverything()
        return self

    def rocketLeagueCrashed(self):
        return ROCKET_LEAGUE_APP_NAME not in WindowMonitor.active_window_names

