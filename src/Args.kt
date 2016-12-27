import java.io.File
import java.lang.System.err
import java.time.ZoneId

data class Args(val kmlDir: File, val imageDir: File, val timeZone: ZoneId, val options: Set<String>) {
  companion object {
    val OPTIONS = mapOf("-v" to "Verbose")

    fun parse(arguments: Array<String>): Args {
      val args = arguments.toMutableList()
      val options = args.intersect(OPTIONS.keys)
      args.removeAll(options)

      if (args.size < 3) {
        printUsage()
        System.exit(1)
      }
      // TODO: we can detect timezones using the coordinates using https://github.com/drtimcooper/LatLongToTimezone
      return Args(File(args[0]), File(args[1]), ZoneId.of(args[2]), options)
    }

    private fun printUsage() {
      err.println("Usage: <kml-dir> <image-dir> <time-zone>")
      err.println("Local time-zone is: ${ZoneId.systemDefault()}, provide the one where the images were taken")
      err.println("Options:")
      OPTIONS.forEach(::println)
    }
  }

  val verbose = options.contains("-v")

  val kmlFiles: Sequence<File>
    get() = kmlDir.list().asSequence().filter { it.endsWith(".kml") || it.endsWith(".xml") }.map { File(kmlDir, it) }

  val imageFiles: Sequence<File>
    get() = imageDir.walkTopDown().filter { it.isFile }
}
