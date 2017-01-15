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

    err.println("Reading tracks from ${args.timelinePath} using ${parser.javaClass.name}...")

    val points = parser.parse(args.timelinePath)
    if (args.verbose) points.forEach { println(it) }
    return points
  }

  fun readImages(): List<Image> {
    err.println("Reading image timestamps from ${args.imageDir}...")

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

  fun outputMatches(images: List<Image>, points: List<TrackPoint>) {
    val out = args.output
    println(out.start())

    Matcher.match(images, points) { image, point ->
      //println("${image.file} ${image.time} ${point} ${track.name}")
      println(out.write(image.file, point))
    }

    println(out.end())
  }
}
