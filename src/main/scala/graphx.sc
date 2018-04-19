import java.io._

import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.LongType
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}

import scala.io.Source
import scala.util.MurmurHash
import scala.util.hashing.MurmurHash3

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._


/* Some code to test */


val df_1 = spark.sqlContext
  .read
  .option("mode", "PERMISSIVE")
  .option("header", "true")
  .option("delimiter", ",")
  .csv("src/main/resources/graphx.csv")


val flightsFromTo = df_1.select($"Origin",$"Dest")
val airportCodes = df_1.select($"Origin", $"Dest").flatMap(x => Iterable(x(0).toString, x(1).toString))

val airportVertices: RDD[(VertexId, String)] = airportCodes.distinct().map(x => (MurmurHash3.stringHash(x).toLong, x)).rdd

val airportEdges:RDD[Edge[String]] = df_1.select($"Origin",$"Dest").as[(String, String)].map{
  case (origin, dest) => Edge(MurmurHash3.stringHash(origin), MurmurHash3.stringHash(dest), s"$origin->$dest")
}.rdd

Graph(airportVertices, airportEdges)


airportCodes.show()
