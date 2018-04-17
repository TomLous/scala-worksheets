import scalaz._
import scalaz.syntax.unzip._
import scalaz.std.option._
import Scalaz._

//val x  =true
//
//x  ? 3.some | none
//
//4.some |+| none
//
//(1, "a") |+| (3, "c")
//
//
//def a: Int=> Int = _ * 3
//def b: Int=> Int = _ + 8
//
//b(a(4))
//
//4 |> a |> b
//
//case class Version(major: Int, minor: Int)
//object Version {
//  def validDigit(digit: Int):Validation[String, Int]  = {
//    (digit > 0) ? digit.success[String] | "FAIL!".failure
//  }
//
//  def validate(major: Int, minor:Int) = {
//    (validDigit(major).toValidationNel |@| validDigit(minor).toValidationNel) {
//      Version(_, _)
//    }
//
//
//
//  }
//}
//
//
//Version.validate(0,0)
//
//Option(null)
val before = "3"
val sep = ","
val after = "4"

val tuple:Option[(String, String)] = Some((before, sep))

// Option[String](before) |+| ".".some |+| Option[String](after)


//Option[String](before) | "0"
//
//List("123".some, ".".some,  "456".some) foldMap( identity)
//
//List("123".some, ".".some,  None) foldMap( identity)
//
//
//List(None, ".".some,  "456".some) foldMap( identity)

tuple.unfzip



//tuple.unfzip





//None.fold("0")
//"some".some.fold("0")







