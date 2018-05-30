import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._

val lines = spark.readStream
  .format("socket")
  .option("host", "localhost")
  .option("port", 9999)
  .load()


val query = lines.writeStream
  .outputMode("complete")
  .format("socket")
  .option("host", "localhost")
  .option("port", 9999)
  .start()

query.awaitTermination()