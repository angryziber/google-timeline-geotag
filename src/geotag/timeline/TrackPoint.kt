package geotag.timeline

import java.time.OffsetDateTime

data class TrackPoint(
  val lat: Latitude,
  val lon: Longitude,
  val time: OffsetDateTime
) {
  constructor(lat: Float, lon: Float, time: OffsetDateTime) : this(Latitude(lat), Longitude(lon), time)
}