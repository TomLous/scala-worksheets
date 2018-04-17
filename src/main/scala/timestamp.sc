import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.from_unixtime

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._

val input = List(
  ("a",1497348453L),
  ("b",1497345453L),
  ("c",1497341453L),
  ("d",1497340453L)
).toDF("name", "timestamp")


input.select(
  'name,
  from_unixtime('timestamp, "yyyy.MM.dd.HH.mm.ss").alias("timestamp_formatted")
).show()
