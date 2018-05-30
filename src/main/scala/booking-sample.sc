val x = Array(1,3,4)

x.contains(5)

val y:Array[Int] = (3 to 9).filter(_ % 2 == 1).toArray

val n = 4

val a = Array.ofDim[Int](n, n)
val lines = List("0 1","2 4", "6 9", "3 5")

for (i <- 0 until n) {
  a(i) = lines(i).split(" ").map(_.trim.toInt)
}

val sets = a.toList.map(r => (r(0) to r(1)).toSet)

val indexedSets = sets.zipWithIndex

val n4 = indexedSets.foldLeft(0){
  case (overlap, (currentSet, currentIndex)) =>
    overlap +
    indexedSets.filter(_._2 != currentIndex).map{
      case (otherSet, _) => if(otherSet.intersect(currentSet).nonEmpty) 1 else 0
    }.sum
}


/*
def checkSets(remainingSets:List[Set[Int]]):Int = remainingSets match {
  case Nil => 0
  case _ :: Nil => 0
  case (set) :: remaining =>
    println(s"Check $set")

    val count = remaining.foldLeft(0){
      case (overlap, otherSet) if set.intersect(otherSet).size != 0 =>
        println(s"Overlap with $otherSet")
        overlap + 1
      case _ => 0
    }

    println(count)
    checkSets(remaining) + count
}


val n3 = checkSets(sets)*/
//tuples match {
//  case (recordFrom, recordTo) :: remaining => remaining.foldLeft(0){
//    case (overlap, (from, to)) if recordFrom >= from && recordFrom <= to => overlap + 1
//    case (overlap, (from, to)) if recordTo >= from && recordTo <= to => overlap + 1
//    case _ => 0
//  }
//}


/*
val tuples = a.toList.map(r => (r(0),r(1)))


def check(remainingTuples:List[(Int, Int)]):Int = remainingTuples match {
  case Nil => 0
  case _ :: Nil => 0
  case (recordFrom, recordTo) :: remaining =>
    println(s"Check $recordFrom - $recordTo")

    val count = remaining.foldLeft(0){
      case (overlap, (from, to)) if recordFrom >= from && recordFrom <= to =>
        println(s"From in range $from - $to")
        overlap + 1
      case (overlap, (from, to)) if recordTo >= from && recordTo <= to =>
        println(s"To in range $from - $to")
        overlap + 1
      case _ => 0
    }

    println(count)
    check(remaining) + count
}


val n2 = check(tuples)
 */





