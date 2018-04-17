
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions._




val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._


//case class Record(ID:Int, DateTime: DateTime)

org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)

//val df = List(
//
//  0 , 2018-01-07T04:04:00 , 25.000, 55.000, OtherThings
//0 , 2018-01-07T04:05:00 , 26.000, 56.000, OtherThings
//1 , 2018-01-07T04:04:00 , 26.000, 50.000, OtherThings
//1 , 2018-01-07T04:05:00 , 27.000, 51.000, OtherThings
//
//  ("id1","id2"),
//  ("id1","id3"),
//  ("id2","id3")
//)
//
//  .toDF("id_x", "id_y")
//
//DF
//  .groupBy(
//    'ID,
//    window('DateTime, "2 minutes")
//  )
//  .agg(
//    mean("latitude").as("latitudeMean"),
//    mean("longitude").as("longitudeMean")
//  )