import java.time.LocalDate

import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.scalacheck.Gen
import org.scalacheck.Prop
import scala.collection.JavaConverters._

import scala.util.Random


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

val dynamicValues: Map[(String, DataType), Gen[Any]] = Map(
  ("a", DoubleType) -> Gen.choose(0.0, 100.0),
  ("aa", StringType) -> Gen.oneOf("happy", "sad", "glad"),
  ("p", LongType) -> Gen.choose(0L, 10L),
  ("pp", StringType) -> Gen.oneOf("Iam", "You're")
)

val schemas = Map(
  "schema1" -> StructType(
    List(
      StructField("a", DoubleType, true),
      StructField("aa", StringType, true),
      StructField("p", LongType, true),
      StructField("pp", StringType, true)
    )),
  "schema2" -> StructType(
    List(
      StructField("a", DoubleType, true),
      StructField("pp", StringType, true),
      StructField("p", LongType, true)
    )
  )
)

val numRecords = 1000

schemas.foreach {
  case (name, schema) =>
    // create a data frame
    spark.createDataFrame(
      // of #numRecords records
      (0 until numRecords).map { _ =>
        // each of them a row
        Row.fromSeq(schema.fields.map(field => {
          // with fields based on the schema's fieldname & type else null
          dynamicValues.get((field.name, field.dataType)).flatMap(_.sample).orNull
        }))
      }.asJava, schema)
      // store to parquet
      .write.mode(SaveMode.Overwrite).parquet(name)
}

/*
 schema.fields.map(field => {
        val values:Seq[Any] = dynamicValues.get(field.name).map(arr =>
          arr(rand.nextInt(arr.length))
        ).orNull
         values
      }))
 */