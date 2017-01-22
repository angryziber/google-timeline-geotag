package geotag.timeline

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import java.io.File
import java.io.IOException
import java.lang.Math.min
import java.lang.System.err
import java.time.Instant

class JsonTimelineParser(val from: Instant, val until: Instant) : TimelineParser {
  val gson = Gson()

  override fun parse(path: File): List<TrackPoint> {
    val result = ArrayList<TrackPoint>(1000)

    var last: JsonObject? = null
    gson.newJsonReader(path.reader()).use { reader ->
      reader.beginObject()
      if (reader.nextName() != "locations") throw IOException("Json file must contain 'locations' array")
      reader.beginArray()
      var proceed = true
      while (reader.hasNext() && proceed) {
        val o = reader.nextObject()
        val time = o.time
        if (time.isAfter(until)) { last = o; continue }
        if (time.isBefore(from)) proceed = false
        result += createPoint(o, time)
      }
    }

    result.reverse()
    if (last != null) result += createPoint(last!!, last!!.time)
    dropInaccuratePoints(result)
    return result
  }

  fun dropInaccuratePoints(points: MutableList<TrackPoint>) {
    if (points.isEmpty()) return
    var last: TrackPoint = points[0]

    var i = 0
    while (++i < points.size) {
      val point = points[i]
      if (point.acc == null) continue

      val d = point.distanceTo(last)
      if (point.acc > 1000 && d > point.acc) {
        val nextAccurate = nextAccurateIndex(points, i-1, d)
        if (nextAccurate != null) {
          val inaccurate = points.subList(i, nextAccurate)
          inaccurate.forEach { err.println("Dropping inaccurate $it") }
          inaccurate.clear()
        }
      }
      last = point
    }
  }

  private fun nextAccurateIndex(points: List<TrackPoint>, start: Int, distanceThreshold: Double): Int? {
    val last = points[start]
    val end = min(start + 30, points.size)
    var i = start
    var prev = last
    while (++i < end) {
      val point = points[i]
      if (point.acc == null) break
      val d = point.distanceTo(last)
      if (point.acc < prev.acc!! && distanceThreshold - d > 1000) return i
      prev = point
    }
    return null
  }

  private fun createPoint(o: JsonObject, time: Instant): TrackPoint {
    return TrackPoint(o["latitudeE7"].e7, o["longitudeE7"].e7, o["altitude"]?.asInt, time, o["accuracy"]?.asInt)
  }

  fun JsonReader.nextObject(): JsonObject = gson.fromJson(this, JsonObject::class.java)

  val JsonObject.time get() = this["timestampMs"].instant

  val JsonElement.instant get() = Instant.ofEpochMilli(asLong)
  val JsonElement.e7 get() = asFloat / 10000000
}