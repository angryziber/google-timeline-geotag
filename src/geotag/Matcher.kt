package geotag

import geotag.images.Image
import geotag.timeline.Track
import java.time.Instant

object Matcher {
  fun match(images: Iterable<Image>, tracks: Iterable<Track>, matchFound: (Image, Track) -> Unit) {
    val ti = tracks.iterator()
    val ii = images.iterator()

    var track = ti.next()
    var image = ii.next()

    do {
      if (track matches image.dateTime) {
        matchFound(image, track)
        image = ii.next()
      }
      else {
        track = ti.skipUntil(image.dateTime) ?: return
      }
    } while (ii.hasNext())
  }

  private fun Iterator<Track>.skipUntil(dateTime: Instant): Track? {
    while (hasNext()) {
      val track = next()
      if (track matches dateTime) return track
    }
    return null
  }

  private infix fun Track.matches(dateTime: Instant) = timeSpan.matches(dateTime)
}