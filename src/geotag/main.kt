package geotag

import geotag.out.GPX
import geotag.out.Output

fun main(arguments: Array<String>) {
  val app = App(Args.parse(*arguments))

  val tracks = app.readKmlTracks()
  val images = app.readImages()

  val out: Output = GPX
  println(out.start())

  Matcher.match(images, tracks) { image, track ->
    val point = track.pointAt(image.dateTime)
    //println("${image.file} ${image.dateTime} ${point} ${track.name}")
    if (point != null) println(out.write(image.file, point))
  }

  println(out.end())
}
