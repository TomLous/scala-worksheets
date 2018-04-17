
object Test extends App {

  val firstLine: String = "Days of Code is awesome!"
  val secondLine: String = null

  val presentation1 = Presentation(firstLine)
  val presentation2 = Presentation(secondLine)

  presentation1.showLower()
  presentation2.showLower()

}


case class Presentation(name: String) {

  def showLower():Unit = {
    println(name.toLowerCase)
  }
}