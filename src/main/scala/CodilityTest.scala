
object CodilityTest extends App{

  // 100%
  object SolutionMissingInteger {
    def solution(a: Array[Int]): Int = {
      val s = a.toSet
      val max = 100000

      (1 to max).toStream.find(i => !s.contains(i)).getOrElse(max + 1)
    }
  }



  println(SolutionMissingInteger.solution(List(1, 3, 6, 4, 1, 2).toArray))


}
