import java.time.Instant
import java.util.*

class App(val args: Args) {
  fun readKmlTracks(): List<Track> {
    val parser = TimelineKmlParser()
    val tracks = ArrayList<Track>(1000)

    args.kmlFiles.forEach { file ->
      try {
        tracks += parser.parse(file)
      } catch (e: Exception) {
        System.err.println("Failed to parse $file: $e")
      }
    }

    tracks.sortBy { it.startAt }
    return tracks
  }

  fun readImages(): List<ImageData> {
    return args.imageFiles.map { file ->
      try {
        ImageData(file, args.timeZone)
      } catch (e: Exception) {
        System.err.println("Failed to read exif from $file: $e")
        null
      }
    }
    .filterNotNull()
    .filter {
      !it.geoTagged.apply {
        if (this) System.err.println("Already geotagged: ${it.file}")
      }
    }.toList().sortedBy { it.dateTime }
  }

  fun match(tracks: Iterable<Track>, images: Iterable<ImageData>) {
    val ti = tracks.iterator()
    val ii = images.iterator()

    var track = ti.next()
    var image = ii.next()

    do {
      if (track.matches(image.dateTime)) {
        matchFound(image, track)
        image = ii.next()
      }
      else {
        track = ti.skipUntil(image.dateTime) ?: return
      }
    } while (ii.hasNext())
  }

  private fun matchFound(image: ImageData, track: Track) {
    println("${image.file} ${image.dateTime} ${track.startAt}-${track.endAt}")
  }

  private fun Track.matches(dateTime: Instant) = startAt <= dateTime && endAt >= dateTime

  private fun Iterator<Track>.skipUntil(dateTime: Instant): Track? {
    while (hasNext()) {
      val track = next()
      if (track.matches(dateTime)) return track
    }
    return null
  }
}
