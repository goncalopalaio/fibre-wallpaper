# fibre-wallpaper

☠ Unsupported️ ☠

Android live wallpaper using OpenGL ES2.0

Current visuals inspired by Martijn Steinrucken LiveCoding - The Universe Within (https://www.youtube.com/watch?v=3CycKKJiwis) guide.

![Screenshot](https://github.com/goncalopalaio/fibre-wallpaper/raw/master/screenshots/2018-07-20%2018_03_08.gif)

To make quick edits to the shader program the shader program is re-compiled when the files change in the sdcard. 
Effectively you can edit the shader program from your computer and see it being updated in realtime (see fibre-wallpaper/shaders/build.sh)

- WallpaperService and EGL surface handling provided by Robert Green - http://www.rbgrn.net/content/354-glsurfaceview-adapted-3d-live-wallpapers

## Known issues:

Render loop appears to not be locking at a constant frame rate.

## TODO

- [ ] Add support to make changes to shaders only using the device.
- [ ] Allow the user to see the current version of the shaders in the settings screen.
- [ ] Allow the user to change colors through the settings screen.
- [ ] Review handling of frame updates and everything that happens in GLWallpaperService. 

