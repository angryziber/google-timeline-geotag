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
    val points = listOf(point(ofEpochSecond(5)), point(ofEpochSecond(10)),
                        point(ofEpochSecond(15)), point(ofEpochSecond(20)), point(ofEpochSecond(30)))
    assertThat(Matcher.collect(images, points))
        .containsExactly(images[0] to points[1], images[1] to points[3], images[2] to points[4])
  }

  @Test fun `images before points are skipped`() {
    val images = listOf(img(ofEpochSecond(100)), img(ofEpochSecond(200)))
    val points = listOf(point(ofEpochSecond(200)), point(ofEpochSecond(300)))
    assertThat(Matcher.collect(images, points)).containsExactly(images[1] to points[0])
  }

  @Test fun `interpolates coordinates for close points`() {
    val images = listOf(img(ofEpochSecond(20)))
    val points = listOf(TrackPoint(0.01f, 0.01f, 10, ofEpochSecond(10)), TrackPoint(0.03f, 0.03f, 30, ofEpochSecond(30)))
    val newPoint = Matcher.collect(images, points)[0].second
    assertThat(newPoint).isEqualTo(TrackPoint(0.02f, 0.02f, 20, ofEpochSecond(20)))
  }

  @Test fun `skips images between distant points`() {
    val images = listOf(img(ofEpochSecond(20)))
    val points = listOf(TrackPoint(10f, 10f, 10, ofEpochSecond(10)), TrackPoint(30f, 30f, 30, ofEpochSecond(30)))
    assertThat(Matcher.collect(images, points)).isEmpty()
  }

  fun img(time: Instant) = Image(File(""), time)
  fun point(time: Instant) = TrackPoint(0f, 0f, 0, time)
}

