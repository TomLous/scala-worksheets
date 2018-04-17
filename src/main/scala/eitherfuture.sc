import cats._
import cats.data._
import cats.implicits._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.Future

val x = 1


type Error = String
type Result = EitherT[Future, Error, String]


def f1 = Future {
  throw new Exception("there is an exception")
  "100"
}

def f2 = Future{
  "200"
}

def f4 = Future{
  "201"
}

def f3 = Future{
  "404"
}

def process(f: Future[String]) = f.map{
  case s if s =="200" => Right(s)
  case s if s =="201" => Right(throw new Exception("oh ok"))
  case s => Left(s)
}.recover{
  case e:Exception => Left(e.getMessage)
}

val comp = for{
  a <- process(f1)
  b <- process(f2)
  c <- process(f3)
  d <- process(f4)
} yield (a,b,c,d)

Await.result(comp, Duration.Inf)
//Await.result(process(f3), Duration.Inf)