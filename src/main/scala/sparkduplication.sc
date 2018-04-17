import java.sql.Date

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Encoders, Row, SparkSession}

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()


import spark.implicits._

case class Item(name: String, date: Date, amount: Int) extends Serializable

org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)


val df = List(
  Item("Jhon", Date.valueOf("2018-4-6"), 100),
  Item("Jhon", Date.valueOf("2018-4-6"), 200),
  Item("Jhon", Date.valueOf("2018-4-13"), 300),
  Item("Jhon", Date.valueOf("2018-4-20"), 500),
  Item("Lee", Date.valueOf("2018-5-4"), 100),
  Item("Lee", Date.valueOf("2018-4-4"), 200),
  Item("Lee", Date.valueOf("2018-5-4"), 300),
  Item("Lee", Date.valueOf("2018-4-11"), 700)
).toDS()


//println(df.collect().toList)
//df.printSchema()

def explodeTuples = udf((tuples: Seq[Row]) => {
  tuples.map {
    case Row(d: Date, a: Int) => (d, a)
  }
    .groupBy(_._1)
    .mapValues(_.map(_._2))
    .toList
    .sortBy(_._1.getTime)
    .foldLeft((List.empty[(Date, Int)], List.empty[Int])) {
      case ((res, acc), dateItem) => {
        val newAcc = acc ++ dateItem._2
        (res ++ newAcc.map(dateItem._1 -> _), newAcc)
      }
    }._1
})


val grouped = df
  .groupBy('name)
  .agg(
    explodeTuples(
      collect_list(
        struct('date, 'amount)
      )
    ).as("tuples")
  )
  .withColumn("exploded", explode('tuples))
  .withColumn("date", $"exploded._1")
  .withColumn("amount", $"exploded._2")
  .drop("exploded", "tuples")

grouped.show(false)
grouped.printSchema()


