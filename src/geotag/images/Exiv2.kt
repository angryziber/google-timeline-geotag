package geotag.images

import geotag.timeline.TrackPoint

object Exiv2 {
  fun geoTag(image: Image, point: TrackPoint): String {
    val lat = point.latitude
    val lon = point.longitude
    return "exiv2 " +
        "-M'set Exif.GPSInfo.GPSLatitudeRef ${lat.ref}' " +
        "-M'set Exif.GPSInfo.GPSLatitude ${lat.d}/1 ${lat.m}/1 ${(lat.s * 1000).toInt()}/1000' " +
        "-M'set Exif.GPSInfo.GPSLongitudeRef ${lon.ref}' " +
        "-M'set Exif.GPSInfo.GPSLongitude ${lon.d}/1 ${lon.m}/1 ${(lon.s * 1000).toInt()}/1000' " +
        image.file
  }
}