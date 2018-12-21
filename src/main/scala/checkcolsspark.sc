import java.time.LocalDate


import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val df1 = List((1, "a"), (2, "B"), (3, "C")).toDF("A", "B")
val df2 = List((1, "a"), (2, "B"), (3, "C")).toDF("A", "X")

val df = df1

val dfWithColX = if (df.columns.contains("X")) {
  df.withColumn("X", coalesce('X, 'A))
} else {
  df.withColumn("X", 'A)
}
df1.withColumn("X", coalesce(df1("X"), 'A)).show()

