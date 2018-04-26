import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import Classes._

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .config("mapreduce.input.fileinputformat.input.dir.recursive","true")
  .getOrCreate()

import spark.implicits._

val prefix = "src/main/resources/rec/"
val filePaths = List("rec1/subrec1.1/", "rec1/subrec1.2/", "rec1/subrec1.1/subsubrec1.1.1/",  "rec2/subrec2.1/").map(prefix + _)
val files = spark.read.text(filePaths: _*)



//= spark.read.csv("src/main/resources/rec/")

files.show(false)
