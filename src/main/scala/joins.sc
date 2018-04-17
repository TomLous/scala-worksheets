import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.{Row, SQLContext, SparkSession}
import org.apache.spark.sql.functions._

val sparkSession = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

val sc = sparkSession.sparkContext
val df1 = sparkSession.read.json("src/main/resources/join1.json")
val df2 = sparkSession.read.json("src/main/resources/join2.json")
import sparkSession.implicits._

df1.show()

df2.show()


df2
  .select('id_df2, explode('array_id_df1).alias("id_df1"))
  .join(df1, usingColumn="id_df1")
  .groupBy('id_df2)
  .agg(collect_list(struct('id_df1, 'values)).alias("array_values"))
  .schema
//  .joinWith(df1.as("a"), $"a.id" === $"b.id").show()
