package geotag.timeline

import java.lang.Math.abs

sealed class LatLon(val value: Float) {
  val abs = abs(value)

  abstract val ref: Char

  val d: Int get() = abs.toInt()
  val m: Int get() = ((abs - d) * 60f).toInt()
  val s: Float get() = (abs - d - m/60f) * 3600f

  override fun equals(other: Any?) = other is LatLon && value == other.value
  override fun hashCode() = value.hashCode()
  override fun toString() = value.toString()
}

class Latitude(value: Float) : LatLon(value) {
  override val ref: Char
    get() = if (value >= 0) 'N' else 'S'
}

class Longitude(value: Float) : LatLon(value) {
  override val ref: Char
    get() = if (value >= 0) 'E' else 'W'
}
