import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .config("spark.mongodb.input.uri", "mongodb://127.0.0.1/dbname")
  .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/dbname")
  .getOrCreate()

import spark.implicits._

val df = List(
  ("abc",1),
  ("def",4),
  ("xyz",23)
).toDF("Word", "Count")

val aggregated = df.agg(
  collect_list(map(lit("word"), 'Word, lit("count"), 'Count)).as("words")
)

MongoSpark.save(aggregated.write.option("collection", "wordcountagg").mode("overwrite"))


// Unsupported literal type class org.apache.spark.sql.Dataset [words: array<map<string,string>>]"


//df.show()



//MongoSpark.save(df.write.option("collection", "wordcount").mode("overwrite"))


//df.repartition(1).write.json("/tmp/wordcount")