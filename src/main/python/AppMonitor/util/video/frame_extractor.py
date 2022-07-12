import math
import os

import cv2

# the positions of each hexagonal subframe on the image to be rendered
subframe_positions = [(0.5, 0.16666666), (0.5, 0.5), (0.5, 0.83333333),
                      (0.66238125, 0), (0.66238125, 0.33333333), (0.66238125, 0.66666666), (0.66238125, 1),
                      (0.33761875, 0), (0.33761875, 0.33333333), (0.33761875, 0.66666666), (0.33761875, 1),
                      (0.82475425, 0.16666666), (0.82475425, 0.5), (0.82475425, 0.83333333),
                      (0.17524375, 0.16666666), (0.17524375, 0.5), (0.17524375, 0.83333333)]


def outOfImageRange(pix, size):
    return pix[0] < 0 or pix[1] < 0 or pix[0] >= size[0] or pix[1] >= size[1]


def compute_subframe_size(image_dimensions):
    # calculating the height and width for each hexagon subframe in the image
    y_scale = 0.309259259  # 1002 / 3240
    subframe_height = image_dimensions[1] * y_scale
    adj = subframe_height / 2
    hyp = adj / math.cos(math.pi / 6)
    subframe_width = hyp * 2
    return subframe_width, subframe_height


def compute_boundaries(subframe_index, image_dimensions):
    subframe_position = (subframe_positions[subframe_index][0]*image_dimensions[0],
                         subframe_positions[subframe_index][1]*image_dimensions[1])
    subframe_size = compute_subframe_size(image_dimensions)
    offsets = (subframe_size[0]/2, subframe_size[1]/2)
    return (subframe_position[0]-offsets[0], subframe_position[1]-offsets[1]),\
           (subframe_position[0]+offsets[0], subframe_position[1]+offsets[1])


def extract_frame(scheduled):
    image_data = ""

    cap = cv2.VideoCapture("../../resources/video/bad.mp4")
    amount_of_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    frame_index = retrieve_next_frame_index(scheduled, amount_of_frames)
    cap.set(1, frame_index[1])
    res, frame = cap.read()

    height, width, channels = frame.shape

    lower_bound, upper_bound = compute_boundaries(frame_index[0], (width, height))
    for y in range(int(lower_bound[1]), int(upper_bound[1])):
        for x in range(int(lower_bound[0]), int(upper_bound[0])):
            if outOfImageRange((x, y), (width, height)):
                image_data += '0'
            else:
                if frame[y, x, 0] > 127:
                    image_data += "1"
                else:
                    image_data += "0"
        image_data += "2"
    image_data += "\n"

    return image_data, frame_index


def retrieve_next_frame_index(scheduled, amount_of_frames):
    best_frame = (-1, -1)

    existing_indexes = []
    for element in os.listdir("./video_data"):
        split_indexes = element.split(".")
        subframe_index = int(split_indexes[0])
        image_index = int(split_indexes[1])
        existing_indexes.append((subframe_index, image_index))
    existing_indexes = set(existing_indexes)

    for j in range(len(subframe_positions)):
        for i in range(amount_of_frames):
            if (j, i) not in existing_indexes:
                best_frame = (j, i)
                if best_frame not in scheduled:
                    return best_frame

    return best_frame
