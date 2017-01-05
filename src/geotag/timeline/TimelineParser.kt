package geotag.timeline

import java.io.File

interface TimelineParser {
  fun parse(path: File): List<Track>
}