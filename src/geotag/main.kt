package geotag

import geotag.images.Exiv2

fun main(arguments: Array<String>) {
  val app = App(Args.parse(arguments))

  val tracks = app.readKmlTracks()
  val images = app.readImages()

  Matcher.match(images, tracks) { image, track ->
    val point = track.pointAt(image.dateTime)
    println("${image.file} ${image.dateTime} ${point} ${track.name}")
    if (point != null) println(Exiv2.geoTag(image, point))
  }
}
