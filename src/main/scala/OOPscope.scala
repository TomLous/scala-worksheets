object OOPscope extends App {

  abstract class A{
    def fff()
  }

  class B extends A{
    override def fff(): Unit = ???
  }

  val b = new B()
  b.fff()

}


