import scala.annotation.tailrec
import scala.collection.mutable.ArrayBuffer

object CodilityTest extends App {


  object SolutionSalad {
    def solution(a: Array[Int], b: Array[Int], c: Array[Int]): Int = {



      val r2 = for{
        aKm <-a
        bKm <-b
        cKm <-c
        if aKm < bKm && bKm < cKm
      } yield (aKm, bKm, cKm)




      r2.length
    }

  }

  println(SolutionSalad.solution(Array(29,50), Array(61,37), Array(37,70)))

  object SolutionGraphCities {


    def solution(t: Array[Int]): Array[Int] = {

      val output = ArrayBuffer.fill(t.length)(0)

      val tIndexed = t zipWithIndex
      val capital = tIndexed.find(e => e._1 == e._2).map(_._1).getOrElse(-1)

      val edges = tIndexed.flatMap {
        case (from, to) => List((from, to), (to, from))
      }.toList.distinct

      val nodes = tIndexed.flatMap {
        case (from, to) => List(from, to)
      }.toList.distinct


      def routeToCapital(current: Int, visited: Set[Int] = Set.empty[Int]): Option[Int] = {
        if (current == capital) Some(0)
        else {
          edges
            .filter(_._1 == current) // all routes from this point
            .map(_._2)
            .filterNot(c => visited.contains(c)) // that have not been vistited
            .flatMap(c => routeToCapital(c, visited + current).map(_ + 1))
          match {
            case Nil => None
            case l => Some(l.min)
          }
        }

      }



      val lengths = nodes.par.map(node => {
        routeToCapital(node).getOrElse(-1)
      }).groupBy(identity).mapValues(_.size).toList

      lengths.foreach{
        case (idx, size)=>output.update(idx, size)
      }

      output.toArray.tail
    }
  }


  println(SolutionGraphCities.solution(
    Array(9, 1, 4, 9, 0, 4, 8, 9, 0, 1)
  ))

  object SolutionCardGame {
    def solution(a: Array[Int], b: Array[Int]): Int = {

      val combinations = b.zipWithIndex.map{
        case (back, pos) =>
          val copyA = a.clone()
          copyA.update(pos, back)
          copyA.toSet
      }


      val max = 100000
      (1 to max).toStream.find(i => {
        combinations.foldLeft(false){
          case (found, set) => found || !set.contains(i)
        }
      }).getOrElse(max+1)


    }
  }


  println(SolutionCardGame.solution(Array(1,2,4,3), Array(1,3,2,4)))  // 2
  println(SolutionCardGame.solution(Array(3,2,1,6,5), Array(4,2,1,3,3))) // 3
  println(SolutionCardGame.solution(Array(1,2), Array(1,2))) // 3

  // 100%
  object SolutionMissingInteger {
    def solution(a: Array[Int]): Int = {
      val s = a.toSet
      val max = 100000

      (1 to max).toStream.find(i => !s.contains(i)).getOrElse(max + 1)
    }
  }



  println(SolutionMissingInteger.solution(List(1, 3, 6, 4, 1, 2).toArray))


  object SolutionFrogRiverOne {
    def solution(x:Int, a: Array[Int]): Int = {


      @tailrec
      def find(l: List[Int], set: Set[Int]=Set.empty[Int], second:Int=0):Option[Int] = l match {
        case Nil => None
        case position :: tail if position <= x && position > 0 =>
          val newSet = set + position
          if(newSet.size == x)
            Some(second)
          else
            find(tail, set + position, second+1)
        case _ :: tail => find(tail, set, second+1)
      }

      find(a.toList).getOrElse(-1)
    }
  }

  println(SolutionFrogRiverOne.solution(5 , List(1, 3, 1, 4, 2, 3, 5,4).toArray))
  println(SolutionFrogRiverOne.solution(5 , List(1, 3, 1, 4, 2, 3, 2,4).toArray))
  println(SolutionFrogRiverOne.solution(5 , List(1, 3, 1, 9, 2, 3, 5,4).toArray))
  println(SolutionFrogRiverOne.solution(5 , List(-4, 3, -1, 9, 2, 3, 5,4).toArray))

}
