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
      val timeSpan = placemark["TimeSpan"]
      val track = Track(placemark["name"].textContent, timeSpan["begin"].instant, timeSpan["end"].instant)

      val coords = placemark.getElementsByTagName("gx:coord")
      val timeStep = track.duration.dividedBy(coords.length.toLong())

      var time = track.startAt
      coords.forEach {
        val (lat, lon) = it.textContent.split(' ')
        track.track += TrackPoint(time, lat.toFloat(), lon.toFloat())
        time += timeStep
      }
      result += track
    }
    return result
  }

  inline fun NodeList.forEach(action: (Element) -> Unit) {
    for (i in 0..length-1) action(item(i) as Element)
  }

  operator fun Element.get(name: String): Element {
    return getElementsByTagName(name).item(0) as Element
  }

  val Element.instant: Instant
      get() = Instant.parse(textContent)
}