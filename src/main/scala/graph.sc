import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.LongType
import org.graphframes.GraphFrame
import scala.util.Try


val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

spark.sparkContext.setCheckpointDir(spark.conf.getOption(s"spark.checkpointdir").getOrElse("/tmp"))

import spark.implicits._

def cleanIds = udf((ids: Seq[String]) => ids.flatMap(x => Try(x.trim.toLong).toOption))

val ds = spark
  .read
  .option("mode", "PERMISSIVE")
  .option("header", "false")
  .option("delimiter", ":")
  .csv("src/main/resources/connections.txt")
  .toDF("id", "links")
  .select(
    'id.cast(LongType),
    cleanIds(split(trim('links), ",")).as("links"))
  .cache()


val vertices = ds.select('id).distinct()


vertices.show()

val edges = ds.select(
  'id.as("src"),
  explode('links).as("dst")
)

edges.show()
edges.printSchema()

val graphFrame = GraphFrame(vertices, edges)

val connectedComponents = graphFrame.connectedComponents.run()

connectedComponents
  .groupBy('component).agg(
  collect_list(struct('id)).as("vertices")
).show(false)
