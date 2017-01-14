package geotag.images

import geotag.out.Exiv2
import geotag.timeline.TrackPoint
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File
import java.time.Instant.now

class Exiv2Test {
  @Test fun `outputs exiv2`() {
    val cmd = Exiv2.write(File("file.cr2"), TrackPoint(59.5050f, 24.3333333f, 100, now()))
    assertThat(cmd).isEqualTo("exiv2 " +
        "-M'set Exif.GPSInfo.GPSLatitudeRef N' " +
        "-M'set Exif.GPSInfo.GPSLatitude 59/1 30/1 18003/1000' " +
        "-M'set Exif.GPSInfo.GPSLongitudeRef E' " +
        "-M'set Exif.GPSInfo.GPSLongitude 24/1 20/1 2/1000' " +
        "-M'set Exif.GPSInfo.GPSAltitudeRef 0' " +
        "-M'set Exif.GPSInfo.GPSAltitude 100/1' " +
        "file.cr2")
  }
}