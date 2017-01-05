package geotag.timeline

import java.io.File

interface TimelineParser {
  fun parse(file: File): List<Track>
}