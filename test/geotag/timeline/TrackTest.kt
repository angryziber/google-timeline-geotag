package geotag.timeline

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import java.time.Instant.now
import java.time.OffsetDateTime

class TrackTest : Spek({
  it("finds an exact single point") {
    val now = now()
    val point = TrackPoint(0f, 0f, OffsetDateTime.now())
    val track = Track("x", TimeSpan(now, now), listOf(point))
    assertThat(track.pointAt(OffsetDateTime.now())).isEqualTo(point)
  }
/*
  it("finds from a range of points") {
    val points = listOf(
        TrackPoint(0f, 0f, Instant.ofEpochSecond(0)),
        TrackPoint(0f, 0f, Instant.ofEpochSecond(10)),
        TrackPoint(0f, 0f, Instant.ofEpochSecond(20))
    )
    val track = Track("x", TimeSpan(now(), now()), points)
    assertThat(track.pointAt(points[1].time)).isEqualTo(points[1])
  }

  it("finds from a range of points") {
    val points = listOf(
        TrackPoint(0f, 0f, Instant.ofEpochSecond(0)),
        TrackPoint(10f, -10f, Instant.ofEpochSecond(10)),
        TrackPoint(20f, -20f, Instant.ofEpochSecond(20)),
        TrackPoint(0f, 0f, Instant.ofEpochSecond(30))
    )
    val track = Track("x", TimeSpan(now(), now()), points)
    assertThat(track.pointAt(Instant.ofEpochSecond(12)))
        .isEqualTo(TrackPoint(12f, -12f, Instant.ofEpochSecond(12)))
  }*/
})