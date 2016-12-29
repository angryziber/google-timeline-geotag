package geotag

fun main(arguments: Array<String>) {
  val app = App(Args.parse(arguments))

  val tracks = app.readKmlTracks()
  val images = app.readImages()

  Matcher.match(images, tracks) { image, track ->
    println("${image.file} ${image.dateTime} ${track.pointAt(image.dateTime)} ${track.name}")
  }
}
