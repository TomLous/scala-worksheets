import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}

val spark = SparkSession.builder().master("local[2]").appName("test").getOrCreate()
import spark.implicits._

val graphData = List(  (0,0,1,10.0),  (1,0,2,5.0),  (2,1,2,2.0),  (3,1,3,1.0),  (4,2,1,3.0),  (5,2,3,9.0),  (6,2,4,2.0),  (7,3,4,4.0),  (8,4,0,7.0),  (9,4,3,5.0)).toDF("edgeid","from","to","distance")

val vertexRDD:RDD[(Long, Int)] = graphData.flatMap(_.getValuesMap[Int](List("to", "from")).values).distinct().map(i => (i.toLong, i)).rdd
val edgeRDD:RDD[Edge[Double]] = graphData.map(x => Edge(x.getInt(1), x.getInt(2), x.getDouble(3))).rdd
val graph:Graph[Int, Double] = Graph(vertexRDD, edgeRDD)


//def vprog(id: VertexId, orgValue: Array[Int], newValue: Array[Int]):Array[Int]= (orgValue ++ newValue).distinct
def vprog(id: VertexId, orig: Array[Long], newly: Array[Long]) : Array[Long] = {
  (orig ++ newly).toSet.toArray
}
//def sendMsg(trip: EdgeTriplet[Array[Int],Double]) : Iterator[(VertexId, Array[Int])] = {
//  val dstList = (trip.dstAttr :+ trip.dstId.toInt).distinct
//  if(trip.srcAttr.intersect(dstList).length != dstList.length) {
//    Iterator((trip.srcId, dstList))
//  }else {
//    Iterator.empty
//  }
//}
def sendMsg(trip: EdgeTriplet[Array[Long],Double]) : Iterator[(VertexId, Array[Long])] = {
  if (trip.srcAttr.intersect(trip.dstAttr ++ Array(trip.dstId)).length != (trip.dstAttr ++ Array(trip.dstId)).toSet.size) {
    Iterator((trip.srcId, (Array(trip.dstId) ++ trip.dstAttr).toSet.toArray ))
  } else Iterator.empty }


//def mergeMsg(a:Array[Int], b:Array[Int]) : Array[Int] = (a ++ b).distinct

def mergeMsg(a: Array[Long], b: Array[Long]) : Array[Long] = {
  (a ++ b).toSet.toArray
}

//val x:Graph[Array[Int], Double] = graph.mapVertices((_, v) => Array(v)).pregel(Array.empty[Int])(
//  vprog,
//  sendMsg,
//  mergeMsg
//)

val x = graph.mapVertices((_, v) => Array(v.toLong)).pregel(Array[Long]())(vprog, sendMsg, mergeMsg)

x.vertices.collect().foreach(println)



  //.pregel(List.empty[VertexId])(vprog, sendMsg, mergeMsg)


//val cc:VertexRDD[VertexId] = graph.connectedComponents().vertices
//cc.toDF().show()

graph.triplets.collect().toList.foreach(println)

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

/*
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.LongType

 */