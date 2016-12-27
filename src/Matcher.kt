import java.time.Instant

object Matcher {
  fun match(tracks: Iterable<Track>, images: Iterable<ImageData>) {
    val ti = tracks.iterator()
    val ii = images.iterator()

    var track = ti.next()
    var image = ii.next()

    do {
      if (track.matches(image.dateTime)) {
        matchFound(image, track)
        image = ii.next()
      }
      else {
        track = ti.skipUntil(image.dateTime) ?: return
      }
    } while (ii.hasNext())
  }

  private fun matchFound(image: ImageData, track: Track) {
    println("${image.file} ${image.dateTime} ${track.startAt}-${track.endAt}")
  }

  private fun Track.matches(dateTime: Instant) = startAt <= dateTime && endAt >= dateTime

  private fun Iterator<Track>.skipUntil(dateTime: Instant): Track? {
    while (hasNext()) {
      val track = next()
      if (track.matches(dateTime)) return track
    }
    return null
  }
}