import cats.syntax.either._

val x:Either[String, String] = Right("xxx")
val y:Either[String, String] = Left("yyy")
val z:Either[String, String] = Right("zzz")

x.flatMap(x => if(x.startsWith("x")) Right(x) else Left("???"))
y.flatMap(x => if(x.startsWith("x")) Right(x) else Left("???"))
z.flatMap(x => if(x.startsWith("x")) Right(x) else Left("???"))