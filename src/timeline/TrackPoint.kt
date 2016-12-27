package timeline

import java.time.Instant

data class TrackPoint(
  val lat: Float,
  val lon: Float,
  val time: Instant
)