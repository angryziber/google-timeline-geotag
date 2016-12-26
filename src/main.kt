import java.io.File

fun main(args: Array<String>) {
  val file = File("history-2016-11-05.xml")

  TimelineKmlParser().parse(file).forEach { place ->
    println("$place")
  }
}
