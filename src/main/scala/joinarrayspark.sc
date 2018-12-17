import org.apache.spark.sql.{SparkSession}
import org.apache.spark.sql.functions._

val sparkSession = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import sparkSession.implicits._

val field = "link"

val df1 = List(
  ("a", List(1, 2, 4, 5)),
  ("b", List(1, 2)),
  ("c", List(6, 7))
).toDF("idA", "link")
val df2 = List(
  ("aa", List(1, 2, 4, 5)),
  ("bb", List(1)),
  ("cc", List(7, 6))
).toDF("idB", "link")

df1
  .joinWith(
    df2, df1(field) === df2(field), "inner")
  .show()




