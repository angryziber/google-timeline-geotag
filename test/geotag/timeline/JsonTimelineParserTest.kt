package geotag.timeline

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Instant.now


class JsonTimelineParserTest {
  val parser = JsonTimelineParser(now(), now())

  @Test fun `drops points that jump to an inaccurate location and back`() {
    val points = listOf(
        TrackPoint(10f, 20f, time=now(), acc=17),
        TrackPoint(11f, 21f, time=now(), acc=1800),
        TrackPoint(11.1f, 21.1f, time=now(), acc=1700),
        TrackPoint(10.01f, 20.01f, time=now(), acc=150)
    )
    val result = points.toMutableList()
    parser.dropInaccuratePoints(result)
    assertThat(result).isEqualTo(listOf(points[0], points[3]))
  }

  @Test fun `doesn't drop points that just move`() {
    val points = listOf(
        TrackPoint(10f, 20f, time=now(), acc=17),
        TrackPoint(11f, 21f, time=now(), acc=1800),
        TrackPoint(11.1f, 21.1f, time=now(), acc=1700),
        TrackPoint(11.11f, 21.11f, time=now(), acc=150)
    )
    val result = points.toMutableList()
    parser.dropInaccuratePoints(result)
    assertThat(result).isEqualTo(points)
  }
}