# Google Timeline Geotag

*Experimental*

The app reads KML files exported from [Google Maps Timeline](https://www.google.com/maps/timeline) 
and a directory of images (recursively), jpeg or raw (tested with Canon CR2). 
Already Geotagged images are skipped.

I have discovered that KML files that you can save from the UI do not contain precise timestamps, so are not
very good for geotagging, especially photograph while moving.

I am going to try to use the [Google Takeout](https://takeout.google.com/settings/takeout/custom/location_history) JSON format instead.

*DateTimeOriginal* Exif tag is read from the image files. Timezone name needs to be
provided as Exif stores only local times. The DST in the provided timezone is accounted for.

Then it matches images to timeline tracks and prints the results.

TODO: write geo positions back to image files.
Maybe also export KML/GPX with detected positions of images?

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
