//import org.json4s._
//import org.json4s.Extraction._
//import org.json4s.native.JsonMethods._
//import scala.reflect.runtime.{universe=>ru}
import io.circe._
import cats.syntax.either._
import io.circe.Decoder.Result
import io.circe.generic.JsonCodec
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import io.circe.parser._

//implicit val formats = org.json4s.DefaultFormats.lossless


@JsonCodec case class User(name:String)
@JsonCodec case class Product(id:String)
@JsonCodec case class Dates(date:Option[java.util.Date])


@JsonCodec case class Meta(count:Int)
@JsonCodec case class ResultList[T](meta: Meta, result: List[T])


// Without generics
@JsonCodec case class ResultListUser(meta: Meta, result: List[User])
@JsonCodec case class ResultListProduct(meta: Meta, result: List[Product])


// general decode method
def decode[T](jsonStr: String): Option[T] = {
  val x= parse(jsonStr)

    .right.get.as[T] match {
    case Right(t) => Some(t)
    case Left(t) => {
      println(t)
      None
    }
  }
}


// data
val userJson = """{"meta":{"count":2},"result":[{"name":"Tom"},{"name":"Lucas"}]}"""
val productJson = """{"meta":{"count":2},"result":[{"id":"123"},{"id":"456"}]}"""
val dateJson = """{"meta":{"count":2},"result":[{"date":"2004-09-04T18:06:22.111Z"},{"date":"2018-01-24T10:06:25.111Z"}]}"""
val customJson = """{"result":{"key":"test"}}"""

val x: Either[ParsingFailure, Result[ResultListUser]] = parse(userJson).map(_.as[ResultListUser])


//val resultListUser = decode[ResultListUser](userJson)
//val resultListProduct = decode[ResultListProduct](productJson)


//val resultListUser2 = decode[ResultList[User]](userJson)
//val resultListProduct2 = decode[ResultList[Product]](productJson)
//val resultListDates2 = decode[ResultList[Dates]](dateJson)

//(decode[JObject](customJson) \\ "key").extract[String]

