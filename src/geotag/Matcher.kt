package geotag

import geotag.images.Image
import geotag.timeline.TrackPoint
import java.util.*

object Matcher {
  fun match(images: Iterable<Image>, points: Iterable<TrackPoint>, matchFound: (Image, TrackPoint) -> Unit) {
    val pi = points.iterator()
    val ii = images.iterator()
    if (!pi.hasNext() || !ii.hasNext()) return

    var point = pi.next()
    var image = ii.next()

    while (true) {
      if (image.time >= point.time) {
        matchFound(image, point)
        if (ii.hasNext()) image = ii.next() else break
      }
      else {
        if (pi.hasNext()) point = pi.next() else break
      }
    }
  }

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