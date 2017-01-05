package geotag

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import java.io.File
import java.time.ZoneId

class ArgsTest: Spek({
  it("parses arguments") {
    val args = Args.parse("sample-kml", "/tmp", "Europe/Tallinn")
    assertThat(args.kmlDir).isEqualTo(File("sample-kml"))
    assertThat(args.imageDir).isEqualTo(File("/tmp"))
    assertThat(args.timeZone).isEqualTo(ZoneId.of("Europe/Tallinn"))
    assertThat(args.verbose).isFalse()
  }

  it("supports options") {
    val args = Args.parse("-v", "sample-kml", "/tmp", "Europe/Tallinn")
    assertThat(args.kmlDir).isEqualTo(File("sample-kml"))
    assertThat(args.imageDir).isEqualTo(File("/tmp"))
    assertThat(args.timeZone).isEqualTo(ZoneId.of("Europe/Tallinn"))
    assertThat(args.options).contains("-v")
    assertThat(args.verbose).isTrue()
  }
})