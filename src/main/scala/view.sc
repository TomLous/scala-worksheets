import scala.collection.SeqView

val xs = List(1,2,3,4,0).view

val ts:SeqView[Either[String, Int] with Product with Serializable, Seq[_]] = xs.map(x => {
  try Right(1 / x)
  catch {
    case ex:ArithmeticException => Left("Something went wrong")
  }

})

val newList = for {t <- ts} println(s"[$t]")

ts.toList.flatMap{
  case Right(x) => Some(x)
  case _ => None
}

def greaterThanZero(x:Int):Option[Int] = if(x > 0) Some(x) else None


greaterThanZero(3)
greaterThanZero(-3)


case class Datlinq[T](numEmployees: Int, departments:T)

object Datlinq{
//   override def apply(numEmployees:Int) = new Datlinq(4)
//
//  def apply(departments: Int): Datlinq = new Datlinq(2 * departments, departments)
//
  val x = 5
}

val d1 = Datlinq(4,2)
val d2 = Datlinq(4,2.4)

//d1.y

//Datlinq(3)


Datlinq.x
//Datlinq.x




//trait Option
//case class Some(value: Any) extends Option
//object None extends Option


//Option
//  .filter{
//  case Right(n)=> n+1
//  case _ => 0
//}

