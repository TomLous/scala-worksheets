import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._

val df = List(
  ("12:00:00","13:00:00"),
  ("12:05:00","13:00:00"),
  ("13:05:00","13:00:00")
).toDF("time1", "time2")

val df1 = df
.withColumn("time1p", to_timestamp('time1, "HH:mm:ss"))
.withColumn("time2p", to_timestamp('time2, "HH:mm:ss"))
  .withColumn("diff",
    datediff(
      to_timestamp($"time1", "HH:mm:ss"),
      to_timestamp($"time2", "HH:mm:ss")
    )
  )
.withColumn("time1i",  to_timestamp('time1, "HH:mm:ss").cast("bigint"))
.withColumn("time2i",  to_timestamp('time2, "HH:mm:ss").cast("bigint"))
.withColumn("diffs", (to_timestamp('time1, "HH:mm:ss").cast("bigint") - to_timestamp('time2, "HH:mm:ss").cast("bigint")) / lit(60))


df1.printSchema()

df1.show()
