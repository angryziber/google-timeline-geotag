package geotag

fun main(arguments: Array<String>) {
  val app = App(Args.Companion.parse(arguments))

  val tracks = app.readKmlTracks()
  val images = app.readImages()

  Matcher.match(tracks, images)
}
