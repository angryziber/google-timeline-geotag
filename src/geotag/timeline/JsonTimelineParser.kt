package geotag.timeline

import me.doubledutch.lazyjson.LazyObject
import java.io.File

class JsonTimelineParser : TimelineParser {
  override fun parse(path: File): List<Track> {
    val obj = LazyObject(path.readText())
    return emptyList()
  }
}