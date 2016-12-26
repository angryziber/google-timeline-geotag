import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import java.time.Duration
import java.time.Instant
import javax.xml.parsers.DocumentBuilderFactory

fun main(args: Array<String>) {
  val file = File("history-2016-11-05.xml")
  val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
  val document = documentBuilder.parse(file.inputStream())
  document.getElementsByTagName("Placemark").forEach { placemark ->
    val name = placemark["name"].textContent
    val begin = placemark["TimeSpan"]["begin"].instant
    val end = placemark["TimeSpan"]["end"].instant
    val coords = placemark.getElementsByTagName("gx:coord")
    val diff = Duration.between(begin, end).dividedBy(coords.length.toLong())

    println("$name: $begin - $end ($diff)")
    var time = begin
    coords.forEach {
      val (lat, lon) = it.textContent.split(' ')
      println("  $lat $lon $time")
      time += diff
    }
  }
}

inline fun NodeList.forEach(action: (Element) -> Unit) {
  for (i in 0..length-1) action(item(i) as Element)
}

operator fun Element.get(name: String): Element {
  return getElementsByTagName(name).item(0) as Element
}

val Element.instant: Instant
    get() = Instant.parse(textContent)