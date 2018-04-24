import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import Classes._
import org.apache.spark.sql.expressions.Window

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

val source = spark.read.option("header", true).csv("src/main/resources/source.csv")
val target = spark.read.option("header", true).csv("src/main/resources/target.csv")

// code from SO
val target1=target.withColumn("version",lit(0))
val target2 = target1.select("id","name","mark1","mark2","version").withColumn("rank", row_number().over(Window.partitionBy("name","mark1","mark2").orderBy("id")))
val diff = source.select("name","mark1","mark2").except(target2.select("name","mark1","mark2"))

diff.show()

if  ( diff.count > 0){
  val diff1 = source.select("name","mark1","mark2") == target2.select("name","mark1","mark2")
  if ( diff1 == false ) {
    val ver = target2.groupBy("name","mark1","mark2").max("rank")
    val common = source.select("name","mark1","mark2").intersect(target2.select("name","mark1","mark2"))
    val result = common.join(ver, common("name") === ver("name") && common("mark1") === ver("mark1") && common("mark2") === ver("mark2"), "inner").select(ver("name"),ver("mark1"),ver("mark2"),ver("max(rank)") as "version")
    val result2 = source.select("name","mark1","mark2").except(target2.select("name","mark1","mark2")).withColumn("version",lit(0))
    val final_result = result.union(result2)
    final_result.show()
  }
}