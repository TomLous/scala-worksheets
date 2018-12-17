import java.time.LocalDate

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val df = List(
  "20070811T013729.163123+0100",
  "20070811T013729.16312+0100",
  "20070811T013729.1631+0100",
  "20070811T013729.163+0100",
  "20070811T013729",
  "20070811"
).toDF("arrivetime")

val df2 = df
  .withColumn("arrivetime2", unix_timestamp(regexp_replace(df("arrivetime"), """(\.\d{3})\d*""", """$1"""), "yyyyMMdd'T'HHmmss.SSSz"))
  .withColumn("str", regexp_replace(df("arrivetime"), """(\.\d{3})\d*""", """$1"""))
  .withColumn("arrivetime3", from_unixtime('arrivetime2))

df2.printSchema()

df2.show(false)