import os

import cv2


def extract_frame(offset=0):
    image_data = ""

    cap = cv2.VideoCapture("C:/Users/Plads/Documents/GitHub/Flyves/BadApple/src/main/python/video_decoder/bad.mp4")
    cap.set(1, retrieve_next_frame_index()+offset)
    res, frame = cap.read()

    width, height, channels = frame.shape

    for x in range(0, width):
        for y in range(0, height):
            if frame[x, y, 0] > 127:
                image_data += "1"
            else:
                image_data += "0"
        image_data += "2"
    image_data += "\n"

    return image_data


def retrieve_next_frame_index():
    best_frame = -1

    for element in os.listdir("C:/Users/Plads/Documents/GitHub/Flyves/BadAppleDropshot/src/main/python/AppMonitor/video_data"):
        frame_index = int(element.split(".")[0])
        if best_frame < frame_index:
            best_frame = frame_index

    return best_frame + 1
