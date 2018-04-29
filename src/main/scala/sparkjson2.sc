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

df.show()
df.printSchema()
