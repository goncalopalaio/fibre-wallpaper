csplit -f current. current.glsl /\/\/===/

mv current.00 current.vert
mv current.01 current.frag

/Users/goncalopalaio/Library/Android/sdk/platform-tools/adb push current.vert /sdcard/fibre/current.vert
/Users/goncalopalaio/Library/Android/sdk/platform-tools/adb push current.frag /sdcard/fibre/current.frag

cp current.vert ../app/src/main/assets/shaders/
cp current.frag ../app/src/main/assets/shaders/