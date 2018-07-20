# fibre-wallpaper

The beginnings of an Android live wallpaper.

The current visuals is built from following Martijn Steinrucken LiveCoding - The Universe Within (https://www.youtube.com/watch?v=3CycKKJiwis) guide

To make quick edits to the shader program the shader program is re-compiled when the files change in the sdcard. 
Effectively you can edit the shader program from your computer and see it being updated in realtime (see fibre-wallpaper/shaders/build.sh)

The current implementation is a draft. I cannot ensure it will work across devices. Works in a OnePlus3T but has issues in a Nexus7 tablet.

![Screenshot](https://github.com/goncalopalaio/fibre-wallpaper/raw/master/screenshots/2018-07-20%2018_03_08.gif)


## Notes:

The render loop is neither optimized or correct in terms of locking the correct framerate.
Generally in the release build, the section that live reloads the shader should not be included. That is yet to be implemented.
Runtime permissions are currently not requested automatically. So to receive file events from the sdcard you will have to grant them through system settings.
