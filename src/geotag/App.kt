package geotag

import geotag.images.Image
import geotag.images.ImageLoader
import geotag.timeline.JsonTimelineParser
import geotag.timeline.KmlTimelineParser
import geotag.timeline.TrackPoint
import java.lang.System.err
import java.time.Instant

class App(val args: Args) {
  fun readTimeline(from: Instant, until: Instant): List<TrackPoint> {
    val parser = if (args.timelinePath.name.endsWith(".json"))
      JsonTimelineParser(from, until) else KmlTimelineParser()

    val points = parser.parse(args.timelinePath) as MutableList
    if (args.verbose) points.forEach { println(it) }
    return points
  }

  fun readImages(): List<Image> {
    val loader = ImageLoader(args.timeZone)
    return args.imageFiles.map { file ->
      try {
        loader.load(file)
      } catch (e: Exception) {
        err.println("Failed to read exif from $file: $e")
        null
      }
    }
    .filterNotNull()
    .apply { if (args.verbose) forEach(::println) }
    .filter {
      !it.geoTagged.apply {
        if (this) err.println("Already geotagged: ${it.file}")
      }
    }.toList().sortedBy { it.time }
  }
}
