import java.util.*

class App(val args: Args) {
  fun readKmlTracks(): ArrayList<Track> {
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
}
