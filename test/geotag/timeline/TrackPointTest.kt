package geotag.timeline

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Instant.ofEpochSecond

class TrackPointTest {
  @Test fun `linear interpolation`() {
    val p1 = TrackPoint(59f, 22f, ofEpochSecond(100))
    val p2 = TrackPoint(63f, 18f, ofEpochSecond(200))

    assertThat(TrackPoint.interpolate(p1, p2, ofEpochSecond(125)))
        .isEqualTo(TrackPoint(60f, 21f, ofEpochSecond(125)))
  }
}