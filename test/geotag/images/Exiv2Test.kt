package geotag.images

import geotag.timeline.TrackPoint
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import java.io.File
import java.time.Instant.now
import java.time.ZoneId

class Exiv2Test: Spek({
  it("outputs exiv2") {
    val cmd = Exiv2.geoTag(Image(File("file.cr2"), ZoneId.systemDefault()), TrackPoint(59.5050f, 24.3333333f, now()))
    assertThat(cmd).isEqualTo("exiv2 " +
        "-M'set Exif.GPSInfo.GPSLatitudeRef N' " +
        "-M'set Exif.GPSInfo.GPSLatitude 59/1 30/1 18003/1000' " +
        "-M'set Exif.GPSInfo.GPSLongitudeRef E' " +
        "-M'set Exif.GPSInfo.GPSLongitude 24/1 20/1 2/1000' " +
        "file.cr2")
  }
})