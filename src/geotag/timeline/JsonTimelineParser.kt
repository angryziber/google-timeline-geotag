package geotag.timeline

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import java.io.File
import java.io.IOException
import java.time.Instant
import java.util.*

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
    return result
  }

  private fun createPoint(o: JsonObject, time: Instant): TrackPoint {
    return TrackPoint(o["latitudeE7"].e7, o["longitudeE7"].e7, o["altitude"].asInt, time, o["accuracy"]?.asInt)
  }

  fun JsonReader.nextObject(): JsonObject = gson.fromJson(this, JsonObject::class.java)

  val JsonObject.time: Instant get() = this["timestampMs"].instant

  val JsonElement.instant: Instant get() = Instant.ofEpochMilli(asLong)
  val JsonElement.e7: Float get() = asFloat / 10000000
}