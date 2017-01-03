package geotag.out

import geotag.timeline.TrackPoint
import java.io.File

object GPX : Output {
  override fun start() = """<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<gpx xmlns="http://www.topografix.com/GPX/1/1">
<trk>
<trkseg>"""

  override fun write(file: File, point: TrackPoint) =
      """<trkpt lat="${point.lat.value}" lon="${point.lon.value}"><time>${point.time}</time><name>${file.name}</name><url>file://${file}</url></trkpt>"""

  override fun end() = """</trkseg>
</trk>
</gpx>"""
}