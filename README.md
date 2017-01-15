# Google Timeline Geotag

The app reads data gathered by [Google Maps Timeline](https://www.google.com/maps/timeline) 
and a directory of images (recursively), jpeg or raw (tested with Canon CR2). 
Already Geotagged images are skipped.

Google Timeline is a little specific, so you will get more precise results with this program compared
to generic geotagging software.

## Input

* **KML** files exported for each day from Google Timeline UI - note that this format is easier to handle, but it
  is imprecise when you have long "tracks" with many points, because the file lacks exact timestamps for each point.
  Also, this format doesn't contain any altitude data.
* **JSON** location history exported using [Google Takeout](https://takeout.google.com/settings/takeout/custom/location_history) -
  this is a more detailed format containing timestamps of each point, it also includes the altitude. 

Although, it is huge, **JSON** format is recommended. The program will read your images first and determine the period,
and then parse the exported JSON (that contains all of your movements since the launch of the service) to find the data
for the correct period before geotagging.

*DateTimeOriginal* Exif tag is read from the image files. Timezone name needs to be
provided as Exif stores only local times. The DST in the provided timezone is accounted for, 
so be sure that your images have correct times, including any DST changes.

## Output

The program itself is non-destructive - it just outputs the commands for `exiv2`, but doesn't itself modify the images.
You will then use the `exiv2` program to actually geotag the images.

GPX output is also supported with `--gpx` in case you want to plot the images on the map first.
You may use [GPS Visualizer](http://www.gpsvisualizer.com/) for this. 

## Tips

If the times of your photos are incorrect, I recommend shifting them first with `exiv2 ad -a [+|-]HH[:MM[:SS[.mmm]]]`

## Building

The code is written in Kotlin, but the only requirement is Java 8 (e.g. *openjdk8*)

`./gradlew assemble`

You will find the resulting jar file in `build/libs`.

## Running

`java -jar build/libs/google-timeline-geotag.jar`

Example with arguments:

`java -jar google-timeline-geotag.jar sample-kml/ ~/Photos/2016/Africa/Nairobi/ Africa/Nairobi`
