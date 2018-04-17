/**
  * Created by Tom Lous on 21/04/17.
  * Copyright © 2017 Datlinq B.V..
  */
object view  extends App{

  val xs = List(1,2,3,4,0)

  val ts = try xs.map(1 / _ )
          catch {
            case ex:ArithmeticException => Nil
          }

  for {t <- ts} println(t)
}
