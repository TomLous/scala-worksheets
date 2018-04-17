trait Error{
  def message: String
  def url: String
}

case class ErrorType1(name: String, url: String) extends Error{
  val message = name + "!!!!"
}


def test(e:Error) = {

  println(e.message)
  println(e.url)

}

test(ErrorType1("Tom", "http:"))



