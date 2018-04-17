import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.apache.spark.sql.functions._




val spark = SparkSession.builder().master("local").appName("jobposting").getOrCreate()

import spark.implicits._

val text = "Apply by mailing your details to: Tom Lous<lous@datlinq.com> with subject I <3 Scala & Spark"

val l  = text.map(_-1)

val l2 = l.grouped(4).zipWithIndex.toMap

val l3 = l2.flatMap{
  case (seq,key) => seq.zipWithIndex.map(r => (r._1, (r._2 + r._1) * (key+3), key *  63))
}

val l4 = scala.util.Random.shuffle(l3.toList)




val df = spark.createDataFrame(l4).toDF("a","b","c")

val f1 = udf{(x:Double, y:Row) => y.getInt(0) + 1}
val f2 = udf{(x:Double, y:Row) => (y.getInt(1) / (x+3)) - y.getInt(0)} // / (x+3)) - y.getInt(0) }
val f3 = udf{(x:Int)=> x.toChar.toString}

val df2 = df.groupBy('c).agg(collect_list(struct('a, 'b)).as("d"))
  .select(('c/lit((2 << 5)-1)).alias("e"),explode('d).as("f")).select('e, f1('e,'f).as("g"),f2('e,'f).as("h"), 'f)
  .sort('e, 'h).select(f3('g).as("i")).agg(concat_ws("",collect_list('i)).as("j"))

df2.show(false)

//val res = df2.collect().toList.flatMap(identity)



//df.show

//val finalRes = l2

//l2.groupBy(_._2)


