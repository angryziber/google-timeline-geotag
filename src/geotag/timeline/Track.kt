package geotag.timeline

data class Track(
  val name: String,
  val timeSpan: TimeSpan,
  val points: List<TrackPoint>
)
