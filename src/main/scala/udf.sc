import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.apache.spark.sql.functions._

val sparkSession = SparkSession.builder()
     .master("local[2]")
     .appName("test")
    .getOrCreate()

val sc = sparkSession.sparkContext
val df = sparkSession.read.json("src/main/resources/udf.json")
import sparkSession.implicits._


val my_size = udf{subjects:Seq[Row] =>if(subjects==null) None else Some(subjects.size)}
val df2 = df.select($"username", my_size($"devices").alias("devcount"))




df2.rdd.repartition(1).saveAsTextFile("src/main/resources/output2")
