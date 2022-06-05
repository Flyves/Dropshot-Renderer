from pywinauto import Desktop


class WindowMonitor:
    active_window_names = []
    created_windows = []
    killed_windows = []

    @staticmethod
    def update():
        new_name_list = []
        for window in Desktop(backend="uia").windows():
            new_name_list.append(window.window_text())

        for process_name in new_name_list:
            if process_name not in WindowMonitor.active_window_names:
                WindowMonitor.active_window_names.append(process_name)
                WindowMonitor.created_windows.append(process_name)
                print('+ ' + process_name)

        for process_name in WindowMonitor.active_window_names:
            if process_name not in new_name_list:
                WindowMonitor.active_window_names.remove(process_name)
                WindowMonitor.killed_windows.append(process_name)
                print('- ' + process_name)
