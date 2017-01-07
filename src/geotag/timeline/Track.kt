package geotag.timeline

import java.time.Duration.between
import java.time.OffsetDateTime

data class Track(
  val name: String,
  val timeSpan: TimeSpan,
  val points: List<TrackPoint>) {

  fun pointAt(time: OffsetDateTime): TrackPoint? {
    val i = points.listIterator(points.size)
    while (i.hasPrevious()) {
      val point = i.previous()
      if (time >= point.time) {
        i.next()
        if (i.hasNext()) return interpolate(point, i.next(), time)
        else return point
      }
    }
    return null
  }

  private fun interpolate(p1: TrackPoint, p2: TrackPoint, time: OffsetDateTime): TrackPoint {
    val c = between(p1.time, time).toMillis().toFloat() / between(p1.time, p2.time).toMillis()
    return TrackPoint(interpolate(p1.lat, p2.lat, c), interpolate(p1.lon, p2.lon, c), time)
  }

  private fun interpolate(v1: LatLon, v2: LatLon, c: Float) = v1.value + (v2.value - v1.value) * c
}
