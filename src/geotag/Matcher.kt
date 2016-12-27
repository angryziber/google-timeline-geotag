package geotag

import geotag.images.Image
import geotag.timeline.Track
import java.time.Instant

object Matcher {
  fun match(tracks: Iterable<Track>, images: Iterable<Image>) {
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

  private fun matchFound(image: Image, track: Track) {
    println("${image.file} ${image.dateTime} ${track.pointAt(image.dateTime)} ${track.name}")
  }

  private fun Iterator<Track>.skipUntil(dateTime: Instant): Track? {
    while (hasNext()) {
      val track = next()
      if (track matches dateTime) return track
    }
    return null
  }

  private infix fun Track.matches(dateTime: Instant) = startAt <= dateTime && endAt >= dateTime
}