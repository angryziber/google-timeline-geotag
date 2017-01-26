package geotag.out

import geotag.timeline.LatLon
import geotag.timeline.TrackPoint
import java.io.File

object Exiv2 : Output {
  override fun write(file: File, point: TrackPoint): String {
    var result = "exiv2 -k " +
      "-M'set Exif.GPSInfo.GPSLatitudeRef ${point.lat.ref}' " +
      "-M'set Exif.GPSInfo.GPSLatitude ${point.lat.deg}' " +
      "-M'set Exif.GPSInfo.GPSLongitudeRef ${point.lon.ref}' " +
      "-M'set Exif.GPSInfo.GPSLongitude ${point.lon.deg}' "

    if (point.alt != null) result +=
      "-M'set Exif.GPSInfo.GPSAltitudeRef 0' " +
      "-M'set Exif.GPSInfo.GPSAltitude ${point.alt}/1' "

    return "$result'$file'"
  }

  private val LatLon.deg: String get() = "${d}/1 ${m}/1 ${(s * 1000).toInt()}/1000"
}