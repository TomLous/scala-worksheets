import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import Classes._
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}
import sun.jvm.hotspot.debugger.cdbg.IntType

val spark = SparkSession.builder()
  .master("local[*]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val source = List(
  (1,1,1,2,2,2),
  (1,1,1,3,3,3),
  (23,23,23,12,12,12)
).toDF("orig1","orig2","orig3","xref1","xref2","xref3")

val target = List(
  (1,1,1,1,1,1,0)
).toDF("orig1","orig2","orig3","xref1","xref2","xref3", "version")


//val target2 = target1.select("id","name","mark1","mark2","version").withColumn("rank", row_number().over())

// Partition by key, orderby version starting with highest version
val w = Window.partitionBy('orig1,'orig2,'orig3).orderBy('version.desc)

val joined = source
  .withColumn("version", lit(null).cast(IntegerType))
  .union(target)
  .withColumn("version", row_number().over(w) + coalesce(max('version).over(w),lit(0)) - lit(1))

joined.show()

