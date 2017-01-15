package geotag

fun main(arguments: Array<String>) {
  val app = App(Args.parse(*arguments))

  val images = app.readImages()
  val points = app.readTimeline(images.first().time, images.last().time)

  app.outputMatches(images, points)
}
