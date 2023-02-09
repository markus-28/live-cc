# live-cc
Live-CC generates live subtitles from a microphone input using the Google Cloud Speech API and overlays them on top of an image. That image is usually a frame of any video-like input or just a static background. That image is then projected onto a Window.

This particular project is a spinoff of another project I worked on. It functions as a playground for myself to enhance my programming skills and optimize the original solution.

## Original Idea
The concept of this project was first introduced in a 2 week university event in January 2023 and was implemented in a group of five.

The idea is to provide live subtitles for lectures either with a presentation or without. Transcriptions should also be sent to a Server and be shown on a Website. This part of the project got implemented using python.

For that, we used a RaspberryPi that gathered audio data and sent a continuous audio stream to the google cloud speech api. Responses were processed and then sent to the video output and a server.
