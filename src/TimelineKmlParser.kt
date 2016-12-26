import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class TimelineKmlParser {
  val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

  fun parse(file: File): List<Placemark> {
    val result = ArrayList<Placemark>()
    val document = documentBuilder.parse(file.inputStream())
    document.getElementsByTagName("Placemark").forEach { placemark ->
      val place = Placemark(placemark["name"].textContent,
          placemark["TimeSpan"]["begin"].instant, placemark["TimeSpan"]["end"].instant)

      val coords = placemark.getElementsByTagName("gx:coord")
      val diff = Duration.between(place.begin, place.end).dividedBy(coords.length.toLong())

      var time = place.begin
      coords.forEach {
        val (lat, lon) = it.textContent.split(' ')
        place.track += TrackPoint(time, lat.toFloat(), lon.toFloat())
        time += diff
      }
      result += place
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