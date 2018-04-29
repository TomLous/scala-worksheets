import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import Classes._

val spark = SparkSession.builder()
  .master("local[*]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val df = spark.read.option("header","true").option("inferSchema","true").json("src/main/resources/complexjson.json")
df.printSchema()

val nested = df
  .select(explode($"data.InterfaceEntity").alias("l1"))
  .select(explode($"l1.l1PhysIf").alias("l2"))
  .select($"l2.rmonIfIn.attributes".alias("l3"))
  .select($"l3.broadcastPkts", $"l3.discards", $"l3.errors", $"l3.multicastPkts", $"l3.packetRate")

  nested.show(false)

nested.printSchema()
