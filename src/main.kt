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

  tracks.forEach {
    println(it)
  }
  println(tracks.size)

  args.imageFiles.map { ImageData(it, args.timeZone) }.forEach {
    // TODO: skip already geotagged images
    println("${it.file} ${it.dateTime}")
  }
}
