object func{


  val l = "1,2,3"

//  l.split(",").flatMap(toInt).toList

  def toInt(s: String):Option[Int] = {
    try{
      Some(s.toInt)
    }
    catch{
      case _ => None
    }

  }

  def parse: String => List[Int] = lst =>  lst.split(",").flatMap(toInt).toList

  parse(l)


}