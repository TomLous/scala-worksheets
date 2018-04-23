import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{SaveMode, SparkSession}

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

/* Some code to test */
//case class TimeData(data: List[String], ColNames: String, Unit: String)
//case class XML(_id: String, TimeData:List[TimeData])
//
//org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)
//
//val df = List(
//  XML("123456A", List(TimeData(List("2011-03-24 11:18:13.350000", "251.23", "130.56"), "dTim,A,B", "?"))),
//  XML("123456A", List(TimeData(List("2011-03-24 11:19:21.310000", "253.23", "140.56"), "dTim,A,B", "?"))),
//  XML("123593X", List(TimeData(List("2011-03-26 12:11:13.350000", "641.13","220.51","10.45"), "dTim,A,B,C", "?"))),
//  XML("123593X", List(TimeData(List("2011-03-26 12:11:13.310000", "641.13","220.51","10.45"), "dTim,A,B,C", "?")))
//).toDS()


val df = spark
  .sqlContext
  .read
  .format("com.databricks.spark.xml")
  .option("rowTag", "log")
  .load("src/main/resources/xml")


// Could be computationally heavy, maybe cache df first if possible, otherwise run it on a sample, otherwise hardcode possible colums
val colNames = df
  .select(explode(split($"TimeData.colNames", ",")).as("col"))
  .distinct()
  .filter($"col" =!= lit("dTim") && $"col" =!= "")
  .collect()
  .map(_.getString(0))
  .toList
  .sorted

// or list all possible columns
//val colNames = List("colA", "colB", "colC")


// Based on XML colNames and data are comma seprated strings that have to be split. Could be done using sql split function, but this UDF maps the columns to the correct field
def mapColsToData = udf((cols: String, data: Seq[String]) =>
  if (cols == null || data == null) Seq.empty[Map[String, String]]
  else {
    data.map(str => (cols.split(",") zip str.split(",")).toMap)
  }
)

//  The result of this action is 1 record for each datapoint for all XML's. Each data record is key->value map of colName->data
val denorm = df.select($"id", explode(mapColsToData($"TimeData.colNames", $"TimeData.data")).as("data"))

denorm.show(false)

// now create column for each map value, based on predef / found columnNames
val columized = denorm.select(
  $"id",
  $"data.dTim".cast(TimestampType).alias("dTim"),
  $"data"
)

columized.show()

// create window over which to resample
val windowSpec = Window
  .partitionBy($"id")
  .orderBy($"dTim".desc)

val resampleRate = 2

// add batchId based on resample rate. Group by batch and
val batched = columized
  .withColumn("batchId", floor((row_number().over(windowSpec) - lit(1)) / lit(resampleRate)))
  .groupBy($"id", $"batchId")
  .agg(collect_list($"data").as("data"))
  .drop("batchId")

batched.show(false)

// Store as 1 huge json file (drop reapatrition if you can handle multiple json, better for master as well)
batched.repartition(1).write.mode(SaveMode.Overwrite).json("/tmp/xml")






//  .groupBy("id").agg(collect_list($"TimeData"))

//val d = df.select("_id", "TimeData.data", "TimeData.ColNames")
//
//// Assume nth is based of dTim ordering
//val windowSpec = Window
//  .partitionBy($"_id")
//  .orderBy($"dTim".desc)
//
//val nthRow  = 2  // define the nthItem to be fetched
//
//df.select(
//  $"_id",
//  $"TimeData.data".getItem(0).getItem(0).cast(TimestampType).alias("dTim"),
//  $"TimeData.data".getItem(0).getItem(1).cast(DoubleType).alias("A"),
//  $"TimeData.data".getItem(0).getItem(2).cast(DoubleType).alias("B"),
//  $"TimeData.data".getItem(0).getItem(3).cast(DoubleType).alias("C")
//).withColumn("n", row_number().over(windowSpec))
//  .filter(col("n") === nthRow)
//  .drop("n")
//.show()


//df.show()
//df.printSchema()