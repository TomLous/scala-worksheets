import cats.syntax.either._

val headers = Map(
  "a" -> "aaaaa",
  "b" -> "bbbbb",
  "c" -> "cccca",
  "d" -> "ddddd",
  "e" -> "eeee"
)
val headerRegexFilter =
  Map(
    "a" -> """[a]+""".r,
    "b" -> """[b]+""".r,
    "c" -> """[c]+""".r,
    "d" -> """[d]+""".r,
    "e" -> """[e]+""".r)


def requiredHeader(name: String): Either[String, String] = {
  headers.get(name) match {
    case Some(x) => Right(x)
    case None => Left("Not Found")
  }
}

Either.cond(!headerRegexFilter.toStream.map{
  case(header, regex) =>
    for{
      headerValue <- requiredHeader(header)
      matches <- Either.cond(regex.pattern.matcher(headerValue).matches,  Unit, s"Header $header value does not match regex")
    } yield matches
}.exists(_.isLeft), Unit, "Message does not match headers")


headerRegexFilter.toStream.forall{
  case(header, regex) =>
    requiredHeader(header).forall(headerValue => regex.pattern.matcher(headerValue).matches)
}
