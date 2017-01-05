package geotag

import geotag.images.Image
import geotag.timeline.JsonTimelineParser
import geotag.timeline.KmlTimelineParser
import geotag.timeline.Track
import java.lang.System.err
import java.time.Instant

class App(val args: Args) {
  fun readTimeline(from: Instant, until: Instant): List<Track> {
    val parser = if (args.timelinePath.name.endsWith(".json"))
      JsonTimelineParser(from, until) else KmlTimelineParser()

    val tracks = parser.parse(args.timelinePath) as MutableList
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
