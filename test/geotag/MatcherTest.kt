package geotag

import geotag.images.Image
import geotag.timeline.TrackPoint
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File
import java.time.Instant
import java.time.Instant.now
import java.time.Instant.ofEpochSecond

class MatcherTest {
  @Test fun `no images`() {
    assertThat(Matcher.collect(listOf(), listOf(point(now())))).isEmpty()
  }

  @Test fun `no points`() {
    assertThat(Matcher.collect(listOf(img(now())), listOf())).isEmpty()
  }

  @Test fun `single image and point`() {
    val time = now()
    val img = img(time)
    val point = point(time)
    assertThat(Matcher.collect(listOf(img), listOf(point))).containsExactly(img to point)
  }

  @Test fun `matches among several points`() {
    val time = ofEpochSecond(20)
    val img = img(time)
    val point = point(time)
    assertThat(Matcher.collect(listOf(img), listOf(point(ofEpochSecond(10)), point, point(ofEpochSecond(30)))))
        .containsExactly(img to point)
  }

  @Test fun `matches several images`() {
    val images = listOf(img(ofEpochSecond(10)), img(ofEpochSecond(20)), img(ofEpochSecond(30)))
    val points = listOf(point(ofEpochSecond(5)), point(ofEpochSecond(10)), point(ofEpochSecond(30)))
    assertThat(Matcher.collect(images, points))
        .containsExactly(images[0] to points[1], images[1] to points[1], images[2] to points[2])
  }

  fun img(time: Instant) = Image(File(""), time)
  fun point(time: Instant) = TrackPoint(0f, 0f, time)
}

