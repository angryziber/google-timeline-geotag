package geotag.timeline

import java.time.Instant

data class TrackPoint(
  val lat: Latitude,
  val lon: Longitude,
  val time: Instant
) {
  constructor(lat: Float, lon: Float, time: Instant) : this(Latitude(lat), Longitude(lon), time)
}