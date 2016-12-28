package geotag.timeline

import java.time.Duration
import java.time.Duration.between
import java.time.Instant

class TimeSpan(val begin: Instant, val end: Instant) {
  val duration: Duration get() = between(begin, end)
  fun matches(dateTime: Instant) = begin <= dateTime && end >= dateTime
}