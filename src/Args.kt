import java.io.File
import java.time.ZoneId

data class Args(val kmlDir: File, val imageDir: File, val timeZone: ZoneId) {
  companion object {
    fun parse(args: Array<String>): Args {
      if (args.size < 3) {
        System.err.println("Usage: <kml-dir> <image-dir> <time-zone>")
        System.err.println("Local time-zone is: ${ZoneId.systemDefault()}, provide the one where the images were taken")
        System.exit(1)
      }
      // TODO: we can detect timezones using the coordinates using https://github.com/drtimcooper/LatLongToTimezone
      return Args(File(args[0]), File(args[1]), ZoneId.of(args[2]))
    }
  }

  val kmlFiles: Sequence<File>
    get() = kmlDir.list().asSequence().filter { it.endsWith(".kml") || it.endsWith(".xml") }.map { File(kmlDir, it) }

  val imageFiles: Sequence<File>
    get() = imageDir.walkTopDown().filter { it.isFile }
}
