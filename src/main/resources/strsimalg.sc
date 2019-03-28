
// Scala example

import scala.math._
import com.rockymadden.stringmetric.similarity._

import scala.collection.immutable.NumericRange
import scala.collection.mutable

def checkAll(s1: String, s2: String) = {
  // Cosine Similarity
  Map(
    "DiceSorensenMetric1" -> DiceSorensenMetric(1).compare(s1, s2),
    "DiceSorensenMetric2" -> DiceSorensenMetric(2).compare(s1, s2),
    "JaccardMetric1" -> JaccardMetric(1).compare(s1, s2),
    "JaccardMetric2" -> JaccardMetric(2).compare(s1, s2),
    "JaccardMetric3" -> JaccardMetric(3).compare(s1, s2),
    "JaroMetric" -> JaroMetric.compare(s1, s2),
    "JaroWinklerMetric" -> JaroWinklerMetric.compare(s1, s2),
    "NGramMetric1" -> NGramMetric(1).compare(s1, s2),
    "NGramMetric2" -> NGramMetric(2).compare(s1, s2),
    "OverlapMetric1" -> OverlapMetric(1).compare(s1, s2),
    "OverlapMetric2" -> OverlapMetric(2).compare(s1, s2),
    "OverlapMetric3" -> OverlapMetric(3).compare(s1, s2),
    "RatcliffObershelpMetric" -> RatcliffObershelpMetric.compare(s1, s2),
    "WeightedLevenshteinMetric" -> WeightedLevenshteinMetric(0.1, 0.1, 0.2).compare(s1, s2)

  )
}

def checks = List(
  (("4547 COUNTY ROAD 601", "4547 COUNTRY ROAD 601"), true),
  (("WASHINGTON STATE UNIV., INT'L PROGS-GLOBAL SERV", "WASHINGTON STATE UNIV., INT'L PROGS-GLOBAL SERVICES BRYAN HALL 108, PO..."), true),
  (("PO BOX 388 2 HARBOR SQUARE", "2 HARBOUR SQUARE"), true),
  (("728 WEST SECOND STR.", "728 W. 2ND. ST."), true),
  (("728 WEST SECOND STR.", "728 W. 2ND STREET P.O. BOX 249  CROWLEY, LA 70527"), true),
  (("181 NORTH 11TH STREET SUITE 307", "181 NORTH 11TH STREET"), true),
  (("191 NORTH 11TH STREET", "181 NORTH 12TH STREET"), false),
  (("2509 Lake Shore Drive N/A", "2509 LAKESHORE DRIVE"), true),
  (("3100 MCKINNON STREET", "3100 McKinnon"), true),
  (("2354 HIGHWAY 64 WEST", "2354 Highwat 64 West"), true),
  (("11785 Northfall Lane, Suite 504", "11785 NORTHFALL LANE"), true),
  (("PIERPONT COMMONS, INTERNATIONAL CENTER 2101 BONISTEEL BLVD.", "PIERPONT COMMONS, LOWER LEVEL"), true),
  (("101 C1 OLD UNDERWOOD ROAD", "1622 OLD UNDERWOOOD ROAD"), false),
  (("2821 TELECOM PARKWAY", "2791 TELECOM PARKWAY"), false),
  (("ROUTE 1 BOX 2", "ROUTE 1 BOX 555"), false),
  (("WASHINGTON STATE UNIV INT L PROGS-GLOBAL SERV","SOMETHING ELSE"), false)
)



val scoreList:Map[String, List[(Boolean, Double)]] = checks.flatMap {
  case ((a, b), result) =>
    val a1 = a.toUpperCase().trim
    val b1 = b.toUpperCase().trim
    val results = checkAll(a1, b1).flatMap{
      case (k, Some(v)) => Some(k -> (v, result))
      case _ => None
    }.toList.sortBy(-_._2._1)

    println("")
    println("")
    println(s"$a <=> $b [$result]")

    results.foreach{
      case (m, (s, _)) =>
        println(s"$m: $s")
    }

    results
}
  .groupBy(_._1)
  .mapValues(_.map{
    case (_, (score, res)) => (res, score)
  })


val threshholdRange= 0.3 to 0.95 by  0.005
val fMeasure = 0.5

val results= threshholdRange.flatMap(threshhold => {
  scoreList.map{
    case (name, scores) =>
      val (tP, tN, fP, fN) = scores.foldLeft((0,0,0,0)){
        case ((tp, tn, fp, fn), (res, score)) if res && score >= threshhold => (tp + 1, tn, fp, fn)
        case ((tp, tn, fp, fn), (res, score)) if res => (tp, tn, fp, fn + 1)
        case ((tp, tn, fp, fn), (res, score)) if !res && score < threshhold => (tp, tn + 1, fp, fn)
        case ((tp, tn, fp, fn), (res, score)) if !res  => (tp, tn, fp + 1, fn)
      }


      val precision = tP.toDouble / (tP + fP + 1e-12)
      val recall = tP.toDouble / (tP + fN + 1e-12)
      val beta = pow(fMeasure,2)

      val fScore = (1 + beta) * precision * recall / (beta * precision + recall + 1e-12)



      (name, threshhold, fScore, precision, recall,tP, tN, fP, fN)
  }

}).toList

results.sortBy(-_._3).foreach(println)
//results.sortBy(-_._4).foreach(println)
//results.sortBy(-_._5).foreach(println)

