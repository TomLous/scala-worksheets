import org.apache.spark.sql
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.functions.{col, lit, map, monotonically_increasing_id}
import org.apache.spark.sql.types.{MapType, StringType, StructType}

import scala.annotation.tailrec

case class Base(id: String, typeName: String)
case class A(override val id: String, override val typeName: String) extends Base(id, typeName)




val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

val linkDataFrame = spark.read.parquet("[path]")

case class LinkReformatted(ids: scala.collection.Map[String, Long],  sources: scala.collection.Map[String, Base])

// Maps each column ending with Id into a Map of (columnname1 (-Id), value1, columnname2 (-Id), value2)
val mapper = linkDataFrame.columns.toList
  .filter(
    _.matches("(?i).*Id$")
  )
  .flatMap(
    c => List(lit(c.replaceAll("(?i)Id$", "")), col(c))
  )

val baseStructType = ScalaReflection.schemaFor[Base].dataType.asInstanceOf[StructType]

val linkDatasetReformatted = linkDataFrame.select(
    map(mapper: _*).alias("ids")
  )
  .withColumn("sources", lit(null).cast(MapType(StringType, baseStructType)))
  .as[LinkReformatted]

@tailrec
def recursiveJoinDatlinqRecords(sourceDataset: Dataset[LinkReformatted], datasets: List[Dataset[Base]]): Dataset[LinkReformatted] = datasets match {
  case Nil => sourceDataset // Nothing left to join, return it
  case baseDataset :: remainingDatasets => {

    val typeName = baseDataset.head.typeName // extract the type from base (each field hase same value)
    val masterName = "source" // something to name the source

    val joinedDataset = sourceDataset.as(masterName) // joining source
      .joinWith(
      baseDataset.as(typeName), // with a base A,B, etc
      col(s"$typeName.id") === col(s"$masterName.ids.$typeName"), // join on source.ids.[typeName]
      "left_outer"
    )
      .map {
        case (source, base) => {
          val newSources = if (source.sources == null) Map(typeName -> base) else source.sources + (typeName -> base) // append or create map of sources
          source.copy(sources = newSources)
        }
      }
      .as[LinkReformatted]
    recursiveJoinDatlinqRecords(joinedDataset, remainingDatasets)
  }
}