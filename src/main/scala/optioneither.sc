import cats._
import cats.data._
import cats.syntax.either._
import cats.implicits._
import cats.instances.list._
import cats.instances.option._
import cats.syntax.traverse._

import scala.util.{Failure, Success, Try}

val opts = List(Some(1), None, Some(0))


def act(i: Int): Double ={
  if(i == 0) throw new Exception("div /0")
  5.0/i
}

def tryToEither(f:Int=>Double)(n: Int): Either[String, Double] = {
  Try(f(n)) match {
    case Success(s) => Right(s)
    case Failure(s) => Left(s.toString)
  }
}


List(Option(1), Option(2), Option(3)).sequence


opts.map(opt=>
  opt.map(number => tryToEither(act)(number)).sequence


)
