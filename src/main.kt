import java.io.File
import java.lang.System.err
import java.util.*

fun main(args: Array<String>) {
  val (kmlDir, imageDir) = readArgs(args)

  val parser = TimelineKmlParser()
  val tracks = ArrayList<Track>(1000)

  kmlDir.listFiles().filter { it.name.endsWith(".kml") || it.name.endsWith(".xml") }.forEach {
    try {
      tracks += parser.parse(it)
    }
    catch (e: Exception) {
      err.println("Failed to parse $it: $e")
    }
  }

  tracks.sortBy { it.startAt }

  tracks.forEach {
    println(it)
  }
  println(tracks.size)

  imageDir.walkTopDown().filter { it.isFile }.forEach { file ->
    println(file)
  }
}

private fun readArgs(args: Array<String>): List<File> {
  if (args.size < 2) {
    err.println("Usage: <kml-dir> <image-dir>")
    System.exit(1)
  }
  return args.map { File(it) }
}
