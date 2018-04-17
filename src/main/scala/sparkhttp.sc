import org.apache.spark.sql.SparkSession
import scala.util.Try
import scalaj.http.Http


val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._

val list = (0 to 100).toDF("application_number").as[Int]

val r = list.map(application_number => {
  Try(Http("https://link").postData(s"""{"searchText": "$application_number","qf":"applId"}""")
    .header("Content-Type", "application/json")
    .asString.body).toOption
}).collect()

println(r.toList)