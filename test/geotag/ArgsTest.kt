package geotag

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.File
import java.time.ZoneId

class ArgsTest {
  @Test fun `parses arguments`() {
    val args = Args.parse("sample-kml", "/tmp", "Europe/Tallinn")
    assertThat(args.timelinePath).isEqualTo(File("sample-kml"))
    assertThat(args.imageDir).isEqualTo(File("/tmp"))
    assertThat(args.timeZone).isEqualTo(ZoneId.of("Europe/Tallinn"))
    assertThat(args.verbose).isFalse()
  }

  @Test fun `supports options`() {
    val args = Args.parse("-v", "sample-kml", "/tmp", "Europe/Tallinn")
    assertThat(args.timelinePath).isEqualTo(File("sample-kml"))
    assertThat(args.imageDir).isEqualTo(File("/tmp"))
    assertThat(args.timeZone).isEqualTo(ZoneId.of("Europe/Tallinn"))
    assertThat(args.options).contains("-v")
    assertThat(args.verbose).isTrue()
  }
}