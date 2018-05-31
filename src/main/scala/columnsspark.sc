
import org.apache.spark.sql.SparkSession

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.catalyst.ScalaReflection
import scala.reflect.runtime.universe._


//object Reflection {
//
//
//  def classAccessors[T: TypeTag]: List[MethodSymbol] = typeOf[T].members.collect {
//    case m: MethodSymbol if m.isCaseAccessor => m
//  }.toList
//
//  def classProperties[T: TypeTag]: List[String] = classAccessors[T].map(_.name.toString)
//
//  def classStructType[T: TypeTag]: StructType = ScalaReflection.schemaFor[T].dataType.asInstanceOf[StructType]
//}



val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._

case class DataFrameExtended(dataFrame: DataFrame) {

  def forceMergeSchema[T: TypeTag]: DataFrame = {
    ScalaReflection
      .schemaFor[T]
      .dataType
      .asInstanceOf[StructType]
      .filterNot(
        field => dataFrame.columns.contains(field.name)
      )
      .foldLeft(dataFrame){
        case (newDf, field) => newDf.withColumn(field.name, lit(null).cast(field.dataType))
      }
  }
}

implicit def dataFrameExtended(df: DataFrame): DataFrameExtended = {
  DataFrameExtended(df)
}



case class CustomData(id: BigInt, colA: Option[String], colB: Option[String])
org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)

val ds = spark
  .read
  .option("mode", "PERMISSIVE")
  .json("src/main/resources/customA.json")
  .as[CustomData]
  .show()



val ds2 = spark
  .read
  .option("mode", "PERMISSIVE")
  .json("src/main/resources/customB.json")
  .forceMergeSchema[CustomData]
  .as[CustomData]
  .show()