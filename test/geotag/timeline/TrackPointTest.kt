package geotag.timeline

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.Instant.ofEpochSecond

class TrackPointTest {
  @Test fun `interpolates linearly`() {
    val p1 = TrackPoint(59f, 22f, 100, ofEpochSecond(100), acc=100)
    val p2 = TrackPoint(63f, 18f, 140, ofEpochSecond(200), acc=100)
    assertThat(TrackPoint.interpolate(p1, p2, ofEpochSecond(125)))
        .isEqualTo(TrackPoint(60f, 21f, 110, ofEpochSecond(125), acc=100))
  }

  @Test fun `interpolates biasing higher accuracy of 1st point`() {
    val p1 = TrackPoint(10f, 10f, 100, ofEpochSecond(100), acc=100)
    val p2 = TrackPoint(12f, 12f, 120, ofEpochSecond(200), acc=1000)
    assertThat(TrackPoint.interpolate(p1, p2, ofEpochSecond(150)))
        .isEqualTo(TrackPoint(10.1f, 10.1f, 101, ofEpochSecond(150), acc=1000))
  }

  @Test fun `interpolates biasing higher accuracy of 2nd point`() {
    val p1 = TrackPoint(10f, 10f, 100, ofEpochSecond(100), acc=1000)
    val p2 = TrackPoint(12f, 12f, 120, ofEpochSecond(200), acc=100)
    assertThat(TrackPoint.interpolate(p1, p2, ofEpochSecond(150)))
        .isEqualTo(TrackPoint(11.9f, 11.9f, 119, ofEpochSecond(150), acc=1000))
  }

  @Test
  fun `interpolates selecting 1st known altitude`() {
    val p1 = TrackPoint(10f, 10f, 100, ofEpochSecond(100), acc=100)
    val p2 = TrackPoint(12f, 12f, null, ofEpochSecond(200), acc=100)
    assertThat(TrackPoint.interpolate(p1, p2, ofEpochSecond(150)))
        .isEqualTo(TrackPoint(11f, 11f, 100, ofEpochSecond(150), acc=100))
  }

  @Test
  fun `interpolates selecting 2nd known altitude`() {
    val p1 = TrackPoint(10f, 10f, null, ofEpochSecond(100), acc=100)
    val p2 = TrackPoint(12f, 12f, 100, ofEpochSecond(200), acc=100)
    assertThat(TrackPoint.interpolate(p1, p2, ofEpochSecond(150)))
        .isEqualTo(TrackPoint(11f, 11f, 100, ofEpochSecond(150), acc=100))
  }
}