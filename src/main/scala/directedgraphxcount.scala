import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}



object directedgraphxcount extends App{

  val spark = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
  import spark.implicits._

//  val graphData = List(  (0,0,1,10.0),  (1,0,2,5.0),  (2,1,2,2.0),  (3,1,3,1.0),  (4,2,1,3.0),  (5,2,3,9.0),  (6,2,4,2.0),  (7,3,4,4.0),  (8,4,0,7.0),  (9,4,3,5.0)).toDF("edgeid","from","to","distance")
  val graphData = List(  (0,0,1,10.0),  (1,0,2,5.0),  (2,1,2,2.0),  (3,1,3,1.0),  (4,2,1,3.0),  (5,2,3,9.0),  (6,2,4,2.0),  (7,3,4,4.0),   (9,4,3,5.0)).toDF("edgeid","from","to","distance")

  val vertexRDD:RDD[(Long, Int)] = graphData.flatMap(_.getValuesMap[Int](List("to", "from")).values).distinct().map(i => (i.toLong, i)).rdd
  val edgeRDD:RDD[Edge[Double]] = graphData.map(x => Edge(x.getInt(1), x.getInt(2), x.getDouble(3))).rdd
  val graph:Graph[Int, Double] = Graph(vertexRDD, edgeRDD)


  def vprog(id: VertexId, orig: List[VertexId], newly: List[VertexId]) : List[VertexId] = newly

  def mergeMsg(a: List[VertexId], b: List[VertexId]) : List[VertexId] = (a ++ b).distinct

  def sendMsg(trip: EdgeTriplet[List[VertexId],Double]) : Iterator[(VertexId, List[VertexId])] = {
//    println(s"${trip.srcId} -> ${trip.dstId} : ${trip.srcAttr} -> ${trip.dstAttr}")
    val recursivelyConnectedNeighbors = (trip.dstId :: trip.dstAttr).filterNot(_ == trip.srcId)

    if (trip.srcAttr.intersect(recursivelyConnectedNeighbors).length != recursivelyConnectedNeighbors.length)
      Iterator((trip.srcId, recursivelyConnectedNeighbors))
    else
      Iterator.empty
  }

  val initList = List.empty[VertexId]

  val result = graph
    .mapVertices((_,_) => initList)
    .pregel(
      initialMsg = initList,
      activeDirection = EdgeDirection.Out
    )(vprog, sendMsg, mergeMsg)
    .mapVertices((_, neighbors) => neighbors.length)

  result.vertices.toDF("vertex", "value").show()



  //.pregel(List.empty[VertexId])(vprog, sendMsg, mergeMsg)


  //val cc:VertexRDD[VertexId] = graph.connectedComponents().vertices
  //cc.toDF().show()

//  graph.triplets.collect().toList.foreach(println)

  //val n:VertexRDD[Array[(VertexId, PartitionID)]] = graph.collectNeighbors(EdgeDirection.Out)
  //
  //val n2:VertexRDD[Array[(VertexId, PartitionID)]] = graph.aggregateMessages(
  //  ctx => ctx.sendToSrc(Array((ctx.dstId, ctx.dstAttr))),
  //  (a, b) => a ++ b, TripletFields.Dst)
  //
  //n.toDF().show(false)
  //n2.toDF().show(false)
  //


  //edgeRDD.toDF().show()
}
