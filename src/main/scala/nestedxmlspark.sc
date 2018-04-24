import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

import org.apache.spark.sql.expressions.Window

val spark = SparkSession.builder()
  .master("local[*]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

// Some classes
case class Grandchild(name:String)
case class Child(name:String, grandchildren: List[Grandchild])
org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)

import spark.implicits._


val children = List(
  ("parent1", None),
  ("parent2", None),
  ("child1", Some("parent1")),
  ("child2", Some("parent1")),
  ("grand1", Some("child1")),
  ("grand2", Some("child1")),
  ("grand3", Some("child1")),
  ("child4", Some("parent1")),
  ("grand4", Some("child4"))
).toDF("name", "root")

val iter1 = children
  .groupBy('root)
  .agg(collect_list('name))
  .withColumn("level", when('root.isNull, lit(0)))


.show(false)