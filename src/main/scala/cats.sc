import cats.syntax.list._
import cats.syntax.either._

  import scala.util.Try


val x = Map(1 -> "a",2 -> "b",3 -> "c").
val y = List().toNel

val z:Either[String, Int] = Either.fromOption(List(1,2,3).toNel,s"Could not find any versions for schema").map(versions => versions.toList.max)
Either.fromOption(List().toNel,s"Could not find any versions for schema")


val w =  "name/name2.txt"

//
//Try(List(1,2,3)).toEither