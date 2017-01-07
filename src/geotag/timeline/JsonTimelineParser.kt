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

    gson.newJsonReader(path.reader()).use { reader ->
      reader.beginObject()
      if (reader.nextName() != "locations") throw IOException("Json file must contain 'locations' array")
      reader.beginArray()
      while (reader.hasNext()) {
        val o = reader.nextObject()
        val time = o["timestampMs"].instant
        if (time.isAfter(until)) continue
        if (time.isBefore(from)) break
        result += TrackPoint(o["latitudeE7"].e7, o["longitudeE7"].e7, time)
      }
    }

    result.reverse()
    return result
  }

  fun JsonReader.nextObject(): JsonObject = gson.fromJson(this, JsonObject::class.java)

  val JsonElement.instant: Instant get() = Instant.ofEpochMilli(asLong)
  val JsonElement.e7: Float get() = asFloat / 10000000
}