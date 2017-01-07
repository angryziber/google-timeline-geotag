package geotag

import java.io.File
import java.lang.System.err
import java.time.ZoneId

data class Args(val timelinePath: File, val imageDir: File, val timeZone: ZoneId, val options: Set<String>) {
  companion object {
    val OPTIONS = mapOf("-v" to "Verbose")

    fun parse(vararg arguments: String): Args {
      val args = arguments.toMutableList()
      val options = args.intersect(OPTIONS.keys)
      args.removeAll(options)

      if (args.size < 3) printUsage()
      // TODO: we can detect timezones using the coordinates using https://github.com/drtimcooper/LatLongToTimezone
      return Args(File(args[0]), File(args[1]), ZoneId.of(args[2]), options)
    }

    private fun printUsage(error: String? = null) {
      if (error != null) err.println("Error: ${error}\n")
      err.println("Usage: <json-file-or-kml-dir> <image-dir> <time-zone>")
      err.println("Local time-zone is: ${ZoneId.systemDefault()}, provide the one where the images were taken")
      err.println("Options:")
      OPTIONS.forEach { err.println(it) }
      System.exit(1)
    }
  }

  init {
    if (!timelinePath.exists()) printUsage("${timelinePath} does not exist")
    if (!imageDir.isDirectory) printUsage("${imageDir} is not a directory")
  }

  val verbose = options.contains("-v")

  val imageFiles: Sequence<File>
    get() = imageDir.walkTopDown().filter { it.isFile }
}
