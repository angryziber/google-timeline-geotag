package geotag.timeline

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import java.time.Instant
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

class KmlTimelineParser : TimelineParser {
  val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

  override fun parse(path: File): List<Track> {
    val kmlFiles = kmlFilesFrom(path)

    val tracks = ArrayList<Track>(1000)
    kmlFiles.forEach { file ->
      try {
        tracks += parseKml(file)
      } catch (e: Exception) {
        System.err.println("Failed to parse $file: $e")
      }
    }
    return tracks
  }

  private fun kmlFilesFrom(path: File) =
    if (path.isDirectory)
        path.list().asSequence().filter { it.endsWith(".kml") || it.endsWith(".xml") }.map { File(path, it) }
    else sequenceOf(path)

  private fun parseKml(file: File): List<Track> {
    val document = documentBuilder.parse(file.inputStream())
    return document.getElementsByTagName("Placemark").map { placemark ->
      val span = placemark["TimeSpan"]
      val timeSpan = TimeSpan(span["begin"].instant, span["end"].instant)
      Track(placemark["name"].textContent, timeSpan,
          points(placemark.getElementsByTagName("gx:coord"), timeSpan))
    }
  }

  private fun points(coords: NodeList, timeSpan: TimeSpan): List<TrackPoint> {
    if (coords.length == 0) return emptyList()
    val timeStep = timeSpan.duration.dividedBy(coords.length.toLong())
    var time = timeSpan.begin - timeStep
    return coords.map {
      val (lon, lat, alt) = it.textContent.split(' ')
      // TODO: set Exif.GPSInfo.GPSAltitudeRef (kml altitudeMode=clampToGround), alt seems to always be 0
      // TODO: set Exif.GPSInfo.GPSAreaInformation to track name
      time += timeStep
      TrackPoint(lat.toFloat(), lon.toFloat(), time)
    }
  }

  private inline fun <T> NodeList.map(transform: (Element) -> T): List<T> {
    val dest = ArrayList<T>(length)
    for (i in 0..length-1) dest += transform(item(i) as Element)
    return dest
  }

  private operator fun Element.get(name: String) = getElementsByTagName(name).item(0) as Element

  private val Element.instant: Instant get() = Instant.parse(textContent)
}