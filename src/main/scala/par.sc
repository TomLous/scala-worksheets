object par{

  val v = Vector.range(0, 10)
  v.foreach(println)
  v.par.foreach(println)

  v.par.foreach{ e => print(e); Thread.sleep(50) }







}