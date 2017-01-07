package geotag.images

import java.io.File
import java.time.Instant

data class Image(val file: File, val time: Instant, val geoTagged: Boolean = false)
