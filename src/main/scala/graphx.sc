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

//
//val df_1 = spark.sqlContext
//  .read
//  .option("mode", "PERMISSIVE")
//  .option("header", "true")
//  .option("delimiter", ",")
//  .csv("src/main/resources/graphx.csv")


val df_1 = spark.sqlContext.read.format("com.databricks.spark.csv").option("header", "true").load("src/main/resources/graphx.csv")

val flightsFromTo = df_1.select($"Origin",$"Dest").rdd
val airportCodes = df_1.select($"Origin", $"Dest").flatMap(x => Iterable(x(0).toString, x(1).toString)).rdd

val airportVertices: RDD[(VertexId, String)] = airportCodes.distinct().map(x => (MurmurHash.stringHash(x), x))
//val airportVertices: RDD[(VertexId, String)] = airportCodes.distinct().map(x => (MurmurHash.stringHash(x), x))
val defaultAirport = ("Missing")

val flightEdges = flightsFromTo.map(x =>
  ((MurmurHash.stringHash(x(0).toString),MurmurHash.stringHash(x(1).toString)), 1)).map(x => Edge(x._1._1, x._1._2,x._2))


val graph = Graph(airportVertices, flightEdges, defaultAirport)
graph.persist() // we're going to be using it a lot


