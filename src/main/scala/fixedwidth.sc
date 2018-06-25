import org.apache.spark.sql.{Dataset, Encoders, SparkSession}
import org.apache.spark.sql.functions._
import Classes._

import scala.util.Try

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._

//case class Measure(val1: Option[String], val2:Option[Double], val3:Option[Double], val4:Option[Double], val5:Option[String], val6:Option[String], val7:Option[Int])
//
//org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)

//AE000041196  25.3330   55.5170   34.0    SHARJAH INTER. AIRP            GSN     41196


spark
  .read
  .textFile("src/main/resources/fixedcol.txt")
    .map(str => {
      val positions = List(11, 20, 30, 37, 72, 75, 85)
      val positionsFromTo = (0 :: positions).sliding(2).map(p => (p(0),p(1))).toList
      val subItems = positionsFromTo.map{
        case (from, to) => Try(str.substring(from, to).trim).toOption
      }

      Measure(
        subItems(0),
        subItems(1).map(_.toDouble),
        subItems(2).map(_.toDouble),
        subItems(3).map(_.toDouble),
        subItems(4),
        subItems(5),
        subItems(6).map(_.toInt)
      )
    })(Encoders.product[Measure])
  .show(false)


