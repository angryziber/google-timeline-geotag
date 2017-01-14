package geotag.timeline

import java.time.Duration
import java.time.Instant

data class TrackPoint(
  val lat: Latitude,
  val lon: Longitude,
  val alt: Int? = null,
  val time: Instant,
  val acc: Int? = null
) {
  constructor(lat: Float, lon: Float, alt: Int? = null, time: Instant, acc: Int? = null) : this(Latitude(lat), Longitude(lon), alt, time, acc)

  companion object {
    fun interpolate(p1: geotag.timeline.TrackPoint, p2: geotag.timeline.TrackPoint, time: Instant): geotag.timeline.TrackPoint {
      val c = Duration.between(p1.time, time).toMillis().toFloat() / Duration.between(p1.time, p2.time).toMillis()
      return TrackPoint(
          interpolate(p1.lat, p2.lat, c),
          interpolate(p1.lon, p2.lon, c),
          if (p1.alt != null && p2.alt != null) interpolate(p1.alt.toFloat(), p2.alt.toFloat(), c).toInt() else null,
          time,
          if (p1.acc != null && p2.acc != null) Math.max(p1.acc, p2.acc) else null)
    }

    private fun interpolate(v1: LatLon, v2: LatLon, c: Float) = interpolate(v1.value, v2.value, c)
    private fun interpolate(v1: Float, v2: Float, c: Float) = v1 + (v2 - v1) * c
  }
}
