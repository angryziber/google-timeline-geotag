import java.io.File
import java.lang.System.err
import java.util.*

fun main(args: Array<String>) {
  val dir = File(".")
  val parser = TimelineKmlParser()
  val tracks = ArrayList<Track>(1000)

  dir.listFiles().filter { it.name.endsWith(".kml") || it.name.endsWith(".xml") }.forEach {
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
}
