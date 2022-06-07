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
import time

from app.rl_keepalive_states import CloseEverything
from app.window_process_monitor import WindowMonitor
from util.logging.logging import ConsoleLogger
from util.network.connection.connection_handler import ServerHandler
from util.state_machine.state_machine import StateMachine


def main():
    start_time = time.time()
    rl_sustainer = StateMachine(CloseEverything())
    server_handler = ServerHandler(ConsoleLogger())
    while True:
        time.sleep(0.5 - ((time.time() - start_time) % 0.5))
        WindowMonitor.update()
        server_handler.update()
        rl_sustainer.exec(server_handler)


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    main()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
