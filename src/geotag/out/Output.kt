package geotag.out

import geotag.timeline.TrackPoint
import java.io.File

interface Output {
  fun start() = ""
  fun write(file: File, point: TrackPoint): String
  fun end() = ""
}