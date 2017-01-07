package geotag.images

import org.apache.commons.imaging.Imaging
import org.apache.commons.imaging.common.ImageMetadata
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata
import org.apache.commons.imaging.formats.tiff.TiffField
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ImageLoader(val timeZone: ZoneId) {
  companion object {
    val EXIF_DATETIME = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
  }

  fun load(file: File): Image {
    val metadata = Imaging.getMetadata(file)
    return Image(file, metadata[ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL].instant,
        metadata[GpsTagConstants.GPS_TAG_GPS_LATITUDE] != null)
  }

  private operator fun ImageMetadata.get(tag: TagInfo): TiffField? {
    return when (this) {
      is TiffImageMetadata -> findField(tag)
      is JpegImageMetadata -> findEXIFValueWithExactMatch(tag)
      else -> null
    }
  }

  private val TiffField?.instant: Instant
    get() = LocalDateTime.parse(this?.stringValue, EXIF_DATETIME).atZone(timeZone).toInstant()
}