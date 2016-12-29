package geotag.timeline

import java.time.Instant

data class TrackPoint(
  val lat: Float,
  val lon: Float,
  val time: Instant
) {
  val latitude: Latitude
    get() = Latitude(lat)

  val longitude: Longitude
    get() = Longitude(lon)
}