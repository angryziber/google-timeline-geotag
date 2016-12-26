fun main(arguments: Array<String>) {
  val app = App(Args.parse(arguments))

  val tracks = app.readKmlTracks()
  tracks.forEach { println(it) }
  println(tracks.size)

  val images = app.readImages()
  images.forEach { println("${it.file} ${it.dateTime}") }
  println(images.size)

  app.match(tracks, images)
}
