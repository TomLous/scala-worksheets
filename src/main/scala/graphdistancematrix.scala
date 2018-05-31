import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}
import org.apache.spark.sql.functions._



object graphdistancematrix extends App {

  val spark = SparkSession.builder().master("local[2]").appName("test").getOrCreate()

  import spark.implicits._

  val graphData = List(
    (0, 0, 1, 10.0),
    (1, 0, 2, 5.0),
    (2, 1, 2, 2.0),
    (3, 1, 3, 1.0),
    (4, 2, 1, 3.0),
    (5, 2, 3, 9.0),
    (6, 2, 4, 2.0),
    (7, 3, 4, 4.0),
    (8, 4, 0, 7.0),
    (9, 4, 3, 5.0)
  ).toDF("id", "from", "to", "distance")

  val vertexRDD: RDD[(Long, Int)] = graphData.flatMap(_.getValuesMap[Int](List("to", "from")).values).distinct().map(i => (i.toLong, i)).rdd
  val edgeRDD: RDD[Edge[Double]] = graphData.map(x => Edge(x.getInt(1), x.getInt(2), x.getDouble(3))).rdd
  val graph: Graph[Int, Double] = Graph(vertexRDD, edgeRDD)


  def vprog(id: VertexId, orig: Map[VertexId, Double], newly: Map[VertexId, Double]): Map[VertexId, Double] = newly

  def mergeMsg(a: Map[VertexId, Double], b: Map[VertexId, Double]): Map[VertexId, Double] = (a.toList ++ b.toList).groupBy(_._1).map{ // mapValues is not serializable :-(
    case (id, v) => id -> v.map(_._2).min // keep shortest distance in case of duplicate
  }

  def sendMsg(trip: EdgeTriplet[Map[VertexId, Double], Double]): Iterator[(VertexId, Map[VertexId, Double])] = {
    val w = trip.attr // weight of edge from src -> dst
    val distances = trip.dstAttr.mapValues(_ + w) + // update collected distances at dst + edge weight
      (trip.srcId -> 0.0, trip.dstId -> w) // set distance to src to 0  and to dst the edge weight
//    println(s"-----\n${trip.srcId} -> ${trip.dstId} \n${trip.srcAttr} -> ${trip.dstAttr}\n${distances}")

    // If src contains as much nodes as dst (we traversed all)
    if(trip.srcAttr.keySet.intersect(distances.keySet).size != distances.keySet.size)
      Iterator((trip.srcId, distances))
    else
      Iterator.empty
  }


  val initMap = Map.empty[VertexId, Double]

  val result = graph
    .mapVertices((_,_) => initMap)
    .pregel(
      initialMsg = initMap,
      activeDirection = EdgeDirection.Out
    )(vprog, sendMsg, mergeMsg)
    .vertices
    .toDF("id","map")
    .select('id, explode('map))
    .groupBy("id")
    .pivot("key")
    .agg(max("value"))
    .orderBy("id")
    .show(false)

//  result.vertices.toDF("vertex", "value").show()

//  result.vertices.toDF().show(false)

  //result.vertices.toDF("vertex", "value").show()
}