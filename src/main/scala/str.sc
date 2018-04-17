object Implicits {
  implicit class ConvertToOption[T](t: T) {
    def optional = Option(t)
  }

  val a:String = null
  val b = "3"

  3.optional
  a.optional
  b.optional

}

