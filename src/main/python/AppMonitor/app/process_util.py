import os
import signal

import psutil
import win32gui
import win32process
from app.window_process_monitor import WindowMonitor


proc_names = []


def get_window_pid(title):
    hwnd = win32gui.FindWindow(None, title)
    thread_id, pid = win32process.GetWindowThreadProcessId(hwnd)
    return pid


def kill_app_with_name(app_name):
    if app_name in WindowMonitor.active_window_names:
        try:
            p = psutil.Process(get_window_pid(app_name))
            p.kill()
        except psutil.NoSuchProcess:
            print('\033[91mkill_app_with_name: couldn\'t kill process!\033[0m')
        except ValueError:
            print('\033[91mkill_app_with_name: couldn\'t kill process!\033[0m')


def kill_process(process_name):
    global proc_names
    for proc in psutil.process_iter():
        if proc.name() == process_name:
            proc.kill()
