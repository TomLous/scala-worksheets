
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}


def lift[T](futures: Seq[Future[T]]) =
  futures.map(
    _.map {
      Success(_)
    }.recover {
      case t => Failure(t)
    })

def futureString(batchNum: Int): Future[Double] = Future {
  val d = Math.random() * 500

  if (batchNum == 2) {
    throw new Exception("!!!!!!!")
  }

  blocking {
    Thread.sleep(d.toLong)
  }
  println(s"Completed $batchNum")

  d

}


val res = (0 to 1000000).toStream.map {
  batchNum => {

    println("New loop")
    val f = futureString(batchNum)

    Await.ready(f, Duration.Inf).value.get
  }
}.dropWhile(_.isSuccess).take(1).toList


