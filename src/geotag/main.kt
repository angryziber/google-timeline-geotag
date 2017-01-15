package geotag

import geotag.out.Exiv2
import geotag.out.Output

fun main(arguments: Array<String>) {
  val app = App(Args.parse(*arguments))

  val images = app.readImages()
  val points = app.readTimeline(images.first().time, images.last().time)

  val out: Output = Exiv2
  println(out.start())

  Matcher.match(images, points) { image, point ->
    //println("${image.file} ${image.time} ${point} ${track.name}")
    println(out.write(image.file, point))
  }

  println(out.end())
}
