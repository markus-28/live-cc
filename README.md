# live-cc

Live-CC is a program that generates live subtitles from a microphone input and overlays them on top of a video. That video is either just a black screen or any arbitrary input.

This particular project is a spinoff of another project I worked on. It is entirely rewritten in Java. Before it was implemented in python. It functions as a playground for myself to enhance my programming skills and optimize the original solution.

## Original Idea
The concept of this project was first introduced and in a 2 week university event in January 2023 and was implemented in a group of five.

The idea is to provide live subtitles for lectures either with a presentation or without. Transcriptions should also be sent to a Server and be shown on a Website.

For that we used a RaspberryPi that gathered audio data and sent a continuous audio stream to the google cloud speech api. Responses are processed and then sent to the video output and a server.
