object reduce {

  val findMax = (x: Int, y: Int) => {
    Thread.sleep(10)
    val winner = x max y
    println(s"compared $x to $y, $winner was larger")
    winner
  }

  val a = Array.range(0, 10)
  a.par.reduce(findMax)

  val x = (1 to 1000).view.map { e =>
    Thread.sleep(10)
    e * 2
  }
  x(40)
}






