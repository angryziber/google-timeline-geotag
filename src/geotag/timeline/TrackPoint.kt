package geotag.timeline

import java.time.Duration
import java.time.Instant

data class TrackPoint(
  val lat: Latitude,
  val lon: Longitude,
  val time: Instant
) {
  constructor(lat: Float, lon: Float, time: Instant) : this(Latitude(lat), Longitude(lon), time)

  companion object {
    fun interpolate(p1: geotag.timeline.TrackPoint, p2: geotag.timeline.TrackPoint, time: Instant): geotag.timeline.TrackPoint {
      val c = Duration.between(p1.time, time).toMillis().toFloat() / Duration.between(p1.time, p2.time).toMillis()
      return TrackPoint(interpolate(p1.lat, p2.lat, c), interpolate(p1.lon, p2.lon, c), time)
    }

    private fun interpolate(v1: LatLon, v2: LatLon, c: Float) = v1.value + (v2.value - v1.value) * c
  }
}
