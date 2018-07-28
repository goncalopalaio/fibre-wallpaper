csplit -f current. current.glsl /\/\/===/

mv current.00 current.vert
mv current.01 current.frag

adb=/Users/goncalopalaio/Library/Android/sdk/platform-tools/adb
for line in `$adb devices | grep -v "List"  | awk '{print $1}'`
do
	device=`echo $line | awk '{print $1}'`
	echo "device -> $device"
	$adb -s $device shell ls /sdcard/fibre/
	$adb -s $device push current.vert /sdcard/fibre/current.vert
	$adb -s $device push current.frag /sdcard/fibre/current.frag
done

cp current.vert ../app/src/main/assets/shaders/
cp current.frag ../app/src/main/assets/shaders/