# Dropshot-Renderer

A Rocket League bot that uses the RLBot framework to render images on the dropshot field. 

## Description

Here is an example of what this program can help render (click on the image to see the video):  
  
[![](https://user-images.githubusercontent.com/34081873/178400527-8bc1b63b-c9f4-41e4-b080-8d8d24028a0b.png)](https://youtu.be/fcR8aT_8CgE)

For your information, this is NOT a real-time run.  
This video took around half a day to render and edit. 


## Getting Started

### Architecture

The following graph shows how the different modules in the program interact:  
  
![](https://user-images.githubusercontent.com/34081873/178389159-37acdd55-f33f-48c0-8c12-5d3f5d80a5f2.png)

Here is a brief summary of what every module does in the program:  
- The `Python Monitor` module:
  - Kills all the processes if a crash occures. This "restart upon crash" feature was needed for me, as speeding the game up to 100x times made it very unstable.  
  - Takes screenshots of the game once it's rendered, and saves them in `./src/main/python/AppMonitor/video_data/`.  
  - When finding the next frame to render, it looks up in `./src/main/python/AppMonitor/video_data/`, and finds the first missing frame it can find. 
If the game ever renders badly a frame (it still occures from time to time as of now), you can safely delete it while the whole program is running. 
It's going to render it again, and nothing will break.  
  - When the game restarts because of a crash, it automatically sends the right combination of keys to the game for bakkesmod to pick it up and set the camera properly. 
It also hides the interface with the "h" key, so you don't have to worry about the spectating overlay. 

- The `Java Server` module:
  - Waits for a socket connection with the `Python Monitor`.
  - Sends requests to get the next frame to be rendered. 
  - Sends requests to take a screenshot of the game. 
  - Speeds the game up to 100x. 
  - Uses state setting to hit the tiles with the ball, score goals to reset the tiles, and move the bot's car out of the way. 

- The `RLBot` module:
  - Forwards the commands sent by the `Java Server`. 
  - Launches `Rocket League`. 

- The `Rocket League` module:
  - Renders tiles in the dropshot game mode. 

- The `Bakkesmod` module:
  - Sets up the in-game camera upon request. 

### Dependencies

* Rocket League
* Bakkesmod (further manual installation steps are needed, see [Installing](#Installing))
* RLBot framework (with the python 3.7 installation)
* Windows
* Java 8 JDK or newer

### Installing

1) Clone this repository
```
git clone https://github.com/Flyves/Dropshot-Renderer
```

2) In `./src/main/python/AppMonitor/userpaths.cfg`, you can set up file locations that are specific to your machine. 
It's important to set up these paths properly, because this is what the `Python Monitor` uses to relaunch everything in case the game crashes.  

3) Install [this bakkesmod plugin](https://bakkesplugins.com/plugins/view/107).  
Then, launch `Rocket League` and `Bakkesmod` manually. Add these 2 bindings to `Bakkesmod`:  
![](https://user-images.githubusercontent.com/34081873/178390324-76cb14a1-bcb2-4ade-b70f-4e58a5f5a04d.png)
![](https://user-images.githubusercontent.com/34081873/178390386-1dc7b8ca-9529-4ffb-a1f7-7e5b6872e86d.png)  


### Executing program

1) Place a black and white version of the video you want to render in `./src/main/resources/video/`. Make sure it is called `bad.mp4`. 
2) Run the `./src/main/python/AppMonitor/main.py` script in administrator. Make sure you are using the python installation that comes with the RLBot framework. 
3) Once the rendering is done, you can use something like [ffmpeg](https://ffmpeg.org/) to create a video from images. 

> Important notice!  
>   
> Be aware that the current version of the program is set up in a way that it splits each frame of the video into 17 seperate parts, tiled hexagonaly.  
I did this because I wanted a better tile resolution out of the renderer. 
If you want to render each entire frame on the field instead of 17 individual parts, you will need to tinker with this script `./src/main/python/AppMonitor/util/video/frame_extractor.py`. 
This is where the frame is seperated into 17 parts.  


### Contact

If you have any questions, feel free to open an issue or contact me directly at lebel.johnw@gmail.com. I'll do my best to answer them. 
