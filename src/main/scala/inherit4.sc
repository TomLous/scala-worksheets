

trait Parser{
  def parse(i: Int):String
}
trait ParserA extends Parser{
  override def parse(i: Int): String = s"A: $i"
}
trait ParserB extends Parser{
  override def parse(i: Int): String = s"B: $i"
}

class BaseClass(val y: Int){
  def show:String = y.toString
}


val n = new BaseClass(4) with ParserA
val m = new BaseClass(3) with ParserB

n.parse(n.y)
m.parse(m.y)

class ExtendedClassA(override val y: Int) extends BaseClass(y) with ParserA{
  override def show = parse(y)
}

class ExtendedClassB(override val y: Int) extends BaseClass(y) with ParserB{
  override def show = parse(y)
}

val o = new ExtendedClassA(6)
val p = new ExtendedClassA(6)

o.show
p.show


