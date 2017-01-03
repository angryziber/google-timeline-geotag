package geotag.out

import geotag.timeline.TrackPoint
import java.io.File

object GPX : Output {
  override fun start() = """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<gpx xmlns="http://www.topografix.com/GPX/1/1">"""

  override fun write(file: File, point: TrackPoint) = """<trk>
  <name>${file}</name>
  <trkseg><trkpt lat="${point.lat.value}" lon="${point.lon.value}"><time>${point.time}</time></trkpt></trkseg>
</trk>"""

  override fun end() = "</gpx>"
}