package geotag

import geotag.images.Image
import geotag.timeline.TrackPoint
import java.lang.System.err
import java.util.*

object Matcher {
  fun match(images: Iterable<Image>, points: Iterable<TrackPoint>, matchFound: (Image, TrackPoint) -> Unit) {
    val pi = points.iterator()
    val ii = images.iterator()

    var image = ii.nextOrNull() ?: return
    var point = pi.nextOrNull() ?: return

    while (image.time < point.time) {
      err.println("Skipping $image - no matching points")
      image = ii.nextOrNull() ?: return
    }

    var nextPoint = pi.nextOrNull() ?: return matchLastPoint(image, point, matchFound)

    while (true) {
      if (image.between(point, nextPoint)) {
        matchFound(image, TrackPoint.interpolate(point, nextPoint, image.time))
        image = ii.nextOrNull() ?: return
      }
      else {
        point = nextPoint
        nextPoint = pi.nextOrNull() ?: return matchLastPoint(image, point, matchFound)
      }
    }
  }

  private fun matchLastPoint(image: Image, point: TrackPoint, matchFound: (Image, TrackPoint) -> Unit) {
    if (image.time == point.time) matchFound(image, point)
  }

  private fun Image.between(p1: TrackPoint, p2: TrackPoint) = time >= p1.time && time < p2.time

  fun <T> Iterator<T>.nextOrNull(): T? = if (hasNext()) next() else null

  fun collect(images: List<Image>, points: List<TrackPoint>): List<Pair<Image, TrackPoint>> {
    val matches = ArrayList<Pair<Image, TrackPoint>>()
    match(images, points) { image, point -> matches += image to point }
    return matches
  }
}