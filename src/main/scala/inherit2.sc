trait Parser{
  def parse(i: Int):String
}
object ParserA extends Parser{
  override def parse(i: Int): String = s"A: $i"
}
object ParserB extends Parser{
  override def parse(i: Int): String = s"B: $i"
}

class BaseClass(parser: Parser, y: Int){
  def show = parser.parse(y)
}

object BaseClass{
  def makeClass = (new BaseClass(_, _)).curried
}

val extendedClassA = BaseClass.makeClass(ParserA)
val extendedClassB = BaseClass.makeClass(ParserB)

val n = extendedClassA(3)
val m = extendedClassA(5)
val o = extendedClassB(2)

n.show
m.show
o.show


