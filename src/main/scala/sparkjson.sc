
import java.time.LocalDate

import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import Classes._

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

def getHistoryReader = udf((val1:Int, val2:Int) => {
  val today = LocalDate.now
  val hdl = List(new HistoryDetail(today.toString, val1, val2, "D"))
  new HistoryHeader(1,today.toString,hdl)
})

val bigDF = spark.read.option("header", true).csv("src/main/resources/ids.csv")
  .select($"int_col1",$"int_col2")
  .withColumn("json_col",to_json(getHistoryReader($"int_col1", $"int_col2")))
bigDF.show(false)

//spark.udf.register("getJSONUDF", getJSON)
//spark.udf.register("getHistoryReaderUDF", getHistoryReader)
//val smallDF = Seq((100, 100), (101, 101), (102, 102)).toDF("int_col1", "int_col2").withColumn("json_col",callUDF("getJSONUDF", $"int_col1", $"int_col2"))

//smallDF.show()
