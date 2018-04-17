
trait Parser{
  def parse(i: Int):String
}
object ParserA extends Parser{
  override def parse(i: Int): String = s"A: $i"
}
object ParserB extends Parser{
  override def parse(i: Int): String = s"B: $i"
}


case class CurriedClass(parser: Parser)(y: Int) {
  def show = parser.parse(y)
}


val extendedClassA = CurriedClass(ParserA) _
val extendedClassB = CurriedClass(ParserB) _

val n = extendedClassA(3)
val m = extendedClassA(5)
val o = extendedClassB(2)

n.show
m.show
o.show



