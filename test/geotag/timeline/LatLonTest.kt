package geotag.timeline

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.jetbrains.spek.api.Spek

class LatLonTest: Spek({
  it("parses Latitude") {
    val lat = Latitude(59.5050f)
    assertThat(lat.ref).isEqualTo('N')
    assertThat(lat.d).isEqualTo(59)
    assertThat(lat.m).isEqualTo(30)
    assertThat(lat.s).isEqualTo(18f, offset(0.01f))
  }

  it("parses Longitude") {
    val lon = Longitude(24.333333333f)
    assertThat(lon.ref).isEqualTo('E')
    assertThat(lon.d).isEqualTo(24)
    assertThat(lon.m).isEqualTo(20)
    assertThat(lon.s).isEqualTo(0f, offset(0.01f))
  }
})