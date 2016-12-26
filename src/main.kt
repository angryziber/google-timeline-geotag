import java.lang.System.err
import java.util.*

fun main(arguments: Array<String>) {
  val args = Args.parse(arguments)

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

  tracks.forEach { println(it) }
  println(tracks.size)

  val images = ImageData.from(args.imageFiles, args.timeZone)
      .filter {
        !it.geoTagged.apply {
          if (this) err.println("Already geotagged: ${it.file}")
        }
      }.toList().sortedBy { it.dateTime }
  images.forEach { println("${it.file} ${it.dateTime}") }
  println(images.size)
}
