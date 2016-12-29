package geotag.images

import geotag.timeline.LatLon
import geotag.timeline.TrackPoint
import java.io.File

object Exiv2 {
  fun geoTag(file: File, point: TrackPoint): String {
    return "exiv2 " +
        "-M'set Exif.GPSInfo.GPSLatitudeRef ${point.lat.ref}' " +
        "-M'set Exif.GPSInfo.GPSLatitude ${point.lat.deg}' " +
        "-M'set Exif.GPSInfo.GPSLongitudeRef ${point.lon.ref}' " +
        "-M'set Exif.GPSInfo.GPSLongitude ${point.lon.deg}' " +
        file
  }

  private val LatLon.deg: String get() = "${d}/1 ${m}/1 ${(s * 1000).toInt()}/1000"
}