import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

import scala.collection.immutable
import scala.util.Random


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test2")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._


val sourceFile = "/tmp/testoverwrite/A"

val init = List(("A", 1), ("B", 1), ("C", 1)).toDF("X", "count")
init.write.mode(SaveMode.Overwrite).parquet(sourceFile)

val rand = Random

(0 to 3).foreach{_ =>
    val A = spark.read.parquet(sourceFile).cache()
    val _ = A.count() // Trigger cache

    val B = (0 to 4).map(_ =>((rand.nextInt(10) + 65).toChar.toString, 1)).toDF("X", "count")
    A.union(B).groupBy('X).agg(sum('count).as("count"))
      .write.mode(SaveMode.Overwrite).parquet(sourceFile)

    A.unpersist(true)
}

val A = spark.read.parquet(sourceFile).show()