import java.time.LocalDate

import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._


val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

def createEmptyDataFrame(schema: StructType): DataFrame = {
  val spark = SparkSession.builder().getOrCreate()
  val dummyRDD = spark.sparkContext.parallelize(Seq(Row.empty))
  spark.createDataFrame(dummyRDD, schema)
}

def resultType(sqlExpression: String, schema: StructType): DataType = {
  val df = createEmptyDataFrame(schema)
  val result = df.withColumn("temp", expr(sqlExpression))
  result.schema("temp").dataType
}

val schema = StructType(
  StructField("mainNaicCode", BooleanType)
    :: StructField("majorSector", StringType)
    :: StructField("naicCode", IntegerType)
    :: StructField("naicCodeCounter", IntegerType)
    :: StructField("naicTitle", StringType)
    :: Nil)
val dt = resultType("LENGTH(naicTitle)", schema)


spark.emptyDataset
val p:Expression = spark.sessionState.sqlParser.parseExpression("LENGTH(naicTitle)")
p.dataType
