package geotag.timeline

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import java.time.Instant
import java.time.Instant.now

class TrackTest : Spek({
  it("finds an exact single point") {
    val now = now()
    val point = TrackPoint(0f, 0f, now)
    val track = Track("x", TimeSpan(now, now), listOf(point))
    assertThat(track.pointAt(now)).isEqualTo(point)
  }

  it("finds from a range of points") {
    val points = listOf(
        TrackPoint(0f, 0f, Instant.ofEpochSecond(0)),
        TrackPoint(0f, 0f, Instant.ofEpochSecond(10)),
        TrackPoint(0f, 0f, Instant.ofEpochSecond(20))
    )
    val track = Track("x", TimeSpan(now(), now()), points)
    assertThat(track.pointAt(points[1].time)).isEqualTo(points[1])
  }
})