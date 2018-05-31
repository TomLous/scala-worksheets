
object view  extends App{

  val xs = List(1,2,3,4,0)

  val ts = try xs.map(1 / _ )
          catch {
            case ex:ArithmeticException => Nil
          }

  for {t <- ts} println(t)
}
