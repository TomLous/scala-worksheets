
trait Parser{
  def parse(i: Int):String
}
implicit object ParserA extends Parser{
  override def parse(i: Int): String = s"A: $i"
}
// only include 1 implicit parser in case of ambiguity
//implicit object ParserB extends Parser{
//  override def parse(i: Int): String = s"B: $i"
//}

case class BaseClass(y: Int)(implicit val parser: Parser){
  def show = parser.parse(y)
}

val x = BaseClass(4)

x.show