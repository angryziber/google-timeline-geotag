package geotag.timeline

import java.time.Instant

data class Track(
  val name: String,
  val timeSpan: TimeSpan,
  val points: List<TrackPoint>) {

  fun pointAt(time: Instant): TrackPoint? {
    return points.findLast { it.time <= time }
  }
}
