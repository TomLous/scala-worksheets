import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions._
import Classes._

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._


case class Person1(name: String, age: Long, gender:String, country:String)
case class PersonType(typeName: String, metadata: Person1)
org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)

val df = Seq(
  PersonType("type1", Person1("Tom",38L,"M", "NL")),
  PersonType("type2", Person1("Marijke",37L,"F", "NL"))
).toDS().withColumnRenamed("typeName", "type")

//df.show()
//df.printSchema()


val df2 = df.select(
  map('type, 'metadata).as("metadata")
)

df2.show(false)
df2.printSchema()
