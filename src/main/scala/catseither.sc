
import cats._
import cats.data._
import cats.implicits._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

type ErrorResp = String

def getList(num: Int): EitherT[Future, ErrorResp, List[String]] = {

  EitherT(Future({
   if(num > 10) Left("Too large")
   else Right((0 to num).map(_.toString).toList)
  }))

}





getList(4)
//Await.result(getList(4).value, Duration.Inf)
//Await.result(getList(14).value, Duration.Inf)

val comb = for {
  l1 <- getList(4).value
  l2 <- getList(14).value
  l3 <- getList(2).value
} yield List(l1,l2,l3)


Await.result(comb, Duration.Inf)


getList(13).value.onComplete{
  case Success(s) => println("Return" + s)
  case Failure(f) => println(f.getMessage); f.getMessage
}