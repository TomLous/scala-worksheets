import sun.jvm.hotspot.debugger.cdbg.BaseClass

trait Parser{
  def parse(i: Int):String
}
object ParserA extends Parser{
  override def parse(i: Int): String = s"A: $i"
}
object ParserB extends Parser{
  override def parse(i: Int): String = s"B: $i"
}

class BaseClass[U <: Parser](parser: U)(str: String)

val x = new BaseClass(ParserA)

//x("5")


