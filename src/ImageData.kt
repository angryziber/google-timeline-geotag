import org.apache.commons.imaging.Imaging
import org.apache.commons.imaging.common.ImageMetadata
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata
import org.apache.commons.imaging.formats.tiff.TiffField
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ImageData(file: File, val timeZone: ZoneId) {
  companion object {
    val EXIF_DATETIME = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")
  }

  val metadata = Imaging.getMetadata(file)

  val dateTime: Instant
    get() = metadata[EXIF_TAG_DATE_TIME_ORIGINAL].instant

  private val TiffField?.instant: Instant
    get() = LocalDateTime.parse(this?.stringValue, EXIF_DATETIME).atZone(timeZone).toInstant()

  private operator fun ImageMetadata.get(tag: TagInfo): TiffField? {
    return when (this) {
      is TiffImageMetadata -> findField(tag)
      is JpegImageMetadata -> findEXIFValueWithExactMatch(tag)
      else -> null
    }
  }
}