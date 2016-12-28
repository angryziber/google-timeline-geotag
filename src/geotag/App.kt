package geotag

import geotag.images.Image
import geotag.timeline.TimelineKmlParser
import geotag.timeline.Track
import java.lang.System.err
import java.util.*

class App(val args: Args) {
  fun readKmlTracks(): List<Track> {
    val parser = TimelineKmlParser()
    val tracks = ArrayList<Track>(1000)

    args.kmlFiles.forEach { file ->
      try {
        tracks += parser.parse(file)
      } catch (e: Exception) {
        err.println("Failed to parse $file: $e")
      }
    }

    tracks.sortBy { it.timeSpan.begin }
    if (args.verbose) tracks.forEach { println(it) }
    return tracks
  }

  fun readImages(): List<Image> {
    return args.imageFiles.map { file ->
      try {
        Image(file, args.timeZone)
      } catch (e: Exception) {
        err.println("Failed to read exif from $file: $e")
        null
      }
    }
    .filterNotNull()
    .apply { if (args.verbose) forEach { println("${it.file} ${it.dateTime}") }}
    .filter {
      !it.geoTagged.apply {
        if (this) err.println("Already geotagged: ${it.file}")
      }
    }.toList().sortedBy { it.dateTime }
  }
}
