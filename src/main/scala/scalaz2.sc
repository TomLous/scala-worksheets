import scalaz._
import Scalaz._


object Implicits {

  implicit class ConvertToOption[T](t: T) {
    def optional = Option(t)
  }
}

/**
  * Created by Tom Lous on 26/08/16.
  * Copyright © 2016 Datlinq B.V..
  */
object FileSize {
  import Implicits._


  val sizeUnits = List("kb","mb","gb","tb","pb","eb","zb","yb")
  type FileSize = Long


  implicit def parse(s: String):Option[FileSize] = {
    val pattern = """(\d+)([\.,](\d+))?\s*(\w+)?""".r

    try {
      val pattern(before, sep, after, unit) = s.trim

      val num:Double = List(before.optional | "0", ".", after.optional | "0").reduce({ _ + _ }).toDouble

      val idx = sizeUnits.indexOf((unit.optional | "").toLowerCase)

      val fs: FileSize = (Math.pow(1024, idx +1 ) * num).toLong

      fs.some
    }catch {
      case _ => None
    }

  }




}

