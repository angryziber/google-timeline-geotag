import java.time.Instant
import java.util.*

data class Placemark(
    val name: String,
    val begin: Instant,
    val end: Instant,
    val track: MutableList<TrackPoint> = ArrayList()
)

data class TrackPoint(
    val time: Instant,
    val lat: Float,
    val lon: Float
)