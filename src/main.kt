fun main(arguments: Array<String>) {
  val app = App(Args.parse(arguments))

  val tracks = app.readKmlTracks()
  val images = app.readImages()

  app.match(tracks, images)
}
