import Classes._
import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}


val spark = SparkSession.builder()
  .master("local[*]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

case class Type1(field1: Int, field2: Int, field3: Int)
case class Type2(field1: Option[Int], field2: Int)

org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)

val df1 = List(
  Type1(1,1,1),
  Type1(2,2,1)
).toDS()

val df2= List(
  Type2(Some(1),1),
  Type2(None,2)
).toDS()


df1.printSchema()
df2.printSchema()

val s1 = df1.schema.fields.map(f => (f.name, f.nullable))
val s2 = df2.schema.fields.map(f => (f.name, f.nullable))

val res = s1 zip s2 forall {
  case ((f1, n1), (f2,n2)) => f1 == f2 && n1 ==  n2
}

(s1 diff s2).length == 0
(s2 diff s1)