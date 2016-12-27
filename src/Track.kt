import java.time.Duration
import java.time.Instant
import java.util.*

data class Track(
  val name: String,
  val startAt: Instant,
  val endAt: Instant,
  val points: MutableList<TrackPoint> = ArrayList()) {
  val duration = Duration.between(startAt, endAt)

  fun pointAt(time: Instant): TrackPoint? {
    return points.findLast { it.time <= time }
  }
}

data class TrackPoint(
  val lat: Float,
  val lon: Float,
  val time: Instant
)