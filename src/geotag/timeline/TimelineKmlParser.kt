package geotag.timeline

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import java.time.Instant
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class TimelineKmlParser {
  val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

  fun parse(file: File): List<Track> {
    val result = ArrayList<Track>()
    val document = documentBuilder.parse(file.inputStream())
    document.getElementsByTagName("Placemark").forEach { placemark ->
      val span = placemark["TimeSpan"]
      val timeSpan = TimeSpan(span["begin"].instant, span["end"].instant)
      result += Track(placemark["name"].textContent, timeSpan,
          points(placemark.getElementsByTagName("gx:coord"), timeSpan))
    }
    return result
  }

  private fun points(coords: NodeList, timeSpan: TimeSpan): List<TrackPoint> {
    if (coords.length == 0) return emptyList()
    val timeStep = timeSpan.duration.dividedBy(coords.length.toLong())
    var time = timeSpan.begin
    val points: MutableList<TrackPoint> = ArrayList()
    coords.forEach {
      val (lat, lon) = it.textContent.split(' ')
      points += TrackPoint(lat.toFloat(), lon.toFloat(), time)
      time += timeStep
    }
    return points
  }

  private inline fun NodeList.forEach(action: (Element) -> Unit) {
    for (i in 0..length-1) action(item(i) as Element)
  }

  private operator fun Element.get(name: String) = getElementsByTagName(name).item(0) as Element

  private val Element.instant: Instant get() = Instant.parse(textContent)
}