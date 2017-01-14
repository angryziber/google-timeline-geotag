package geotag

import geotag.images.Image
import geotag.timeline.TrackPoint
import java.util.*

object Matcher {
  fun match(images: Iterable<Image>, points: Iterable<TrackPoint>, matchFound: (Image, TrackPoint) -> Unit) {
    val pi = points.iterator()
    val ii = images.iterator()

    var image = ii.nextOrNull() ?: return
    var point = pi.nextOrNull() ?: return
    var nextPoint = pi.nextOrNull() ?: return matchLastPoint(image, point, matchFound)

    while (true) {
      if (image.between(point, nextPoint)) {
        matchFound(image, point)
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

  /*
  private fun interpolate(p1: TrackPoint, p2: TrackPoint, time: Instant): TrackPoint {
    val c = Duration.between(p1.time, time).toMillis().toFloat() / Duration.between(p1.time, p2.time).toMillis()
    return TrackPoint(interpolate(p1.lat, p2.lat, c), interpolate(p1.lon, p2.lon, c), time)
  }

  private fun interpolate(v1: LatLon, v2: LatLon, c: Float) = v1.value + (v2.value - v1.value) * c
  */
}