val either1 = Right(List("a"->3, "b"->4).toMap)
val either2 = Left("ERROR")


for{
  e <- either1.right
  d <- e.get("a")
  f <- Right(d)

//  d.map(Right(_)).getOrElse(Left("Not found" + "a")).right
} yield f
//
//either1.right.flatMap(_.get("b") match {
//  case Some(x) => Right(x)
//  case None => Left("error")
//})

//for{
//  e <- either1
//
//} yield e


