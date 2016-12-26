import java.io.File
import java.lang.System.err
import java.time.ZoneId
import java.util.*

fun main(args: Array<String>) {
  val args = Args.parse(args)

  val parser = TimelineKmlParser()
  val tracks = ArrayList<Track>(1000)

  args.kmlFiles.forEach { file ->
    try {
      tracks += parser.parse(file)
    }
    catch (e: Exception) {
      err.println("Failed to parse $file: $e")
    }
  }

  tracks.sortBy { it.startAt }

  tracks.forEach {
    println(it)
  }
  println(tracks.size)

  args.imageFiles.forEach { file ->
    val imageData = ImageData(file, args.timeZone)
    // TODO: skip already geotagged images
    println("$file ${imageData.dateTime}")
  }
}

data class Args(val kmlDir: File, val imageDir: File, val timeZone: ZoneId) {
  companion object {
    fun parse(args: Array<String>): Args {
      if (args.size < 3) {
        err.println("Usage: <kml-dir> <image-dir> <time-zone>")
        err.println("Local time-zone is: ${ZoneId.systemDefault()}, provide the one where the images were taken")
        System.exit(1)
      }
      // TODO: we can detect timezones using the coordinates using https://github.com/drtimcooper/LatLongToTimezone
      return Args(File(args[0]), File(args[1]), ZoneId.of(args[2]))
    }
  }

  val kmlFiles: Sequence<File>
    get() = kmlDir.list().asSequence().filter { it.endsWith(".kml") || it.endsWith(".xml") }.map(::File)

  val imageFiles: Sequence<File>
    get() = imageDir.walkTopDown().filter { it.isFile }
}
