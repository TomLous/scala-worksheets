object lift{

  def targetMethod(a: String, b: String) = s"$a and $b"


  targetMethod("tom", "edoardo")


  val maybe1:Option[String] = Some("1")
  val maybe2:Option[String] = Some("2")
  val maybe3:Option[String] = None


  def wrapper[T,U](f: (T,T)=>U)(a: Option[T], b:Option[T]):Option[U] = {
    (a,b) match {
      case (Some(a1), Some(b1)) => Some(f(a1,b1))
      case _ => None
    }
  }

  wrapper(targetMethod)(maybe1, maybe2)
  wrapper(targetMethod)(maybe1, maybe3)

  val targetWrap = wrapper(targetMethod) _


  targetWrap(maybe2, maybe1)
  targetWrap(maybe3, maybe1)

  val func = (a:String, b:String) => {
    a.length + b.length
  }



//  def wr[T,U](f: (T,T)=>U)(a: Option[T], b:Option[T]):Option[U] = {
//    (a, b) match {
//      case (Some(a1), Some(b1)) => Some(f(a1, b1))
//      case _ => None
//    }
//  }
//
//
//
//
// "t"


}