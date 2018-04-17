import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType


val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._

val t1 = List(
  ("id1","id2"),
  ("id1","id3"),
  ("id2","id3")
)

  .toDF("id_x", "id_y")


val t2 = List(
  ("id1","blue","m"),
  ("id2","red","s"),
  ("id3","blue","s")
).toDF("id", "color", "size")

t1
  .join(t2.as("x"), $"id_x" === $"x.id", "inner")
  .join(t2.as("y"), $"id_y" === $"y.id", "inner")
  .select(
    'id_x,
    'id_y,
    when($"x.color" === $"y.color",1).otherwise(0).alias("color").cast(IntegerType),
    when($"x.size" === $"y.size",1).otherwise(0).alias("size").cast(IntegerType)
  )
  .show()


println("test")