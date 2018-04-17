import java.util.Properties

import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
//import scala.reflect.runtime.universe._
import scala.util._

implicit val formats = org.json4s.DefaultFormats

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

def jsonStringify = udf((obj: AnyRef) => {
  if(obj == null) Option.empty[String]
  else {
    val res:Option[String] = Try(Serialization.write(obj)).toOption
//    val jsonstr = Serialization.write(obj)
//    val jsonstr = "3"
//    println(obj)
    res
  }
})

import spark.implicits._

val prop = new Properties()
prop.put("user", "root")
prop.put("driver", "com.mysql.cj.jdbc.Driver")


spark.read.json("/Users/tomlous/Downloads/lu.json")
    .select(
      lit(6).as("batchId"),
      $"input_record.id_name".as("name"),
      to_json('input_record).as("recordA"),
      to_json('best_match).as("recordB"),
      to_json('match_meta).as("matchMeta"),
      to_json('match_details).as("matchDetails"),
      'confidence.as("confidence")
    )
    .filter('recordB =!= "{}")
  .write
  .mode("append") //.option("truncate", true)
  .jdbc(s"jdbc:mysql://localhost/annotations?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "match_pair", prop)
//  .format("jdbc")
//  .option("url", "jdbc:mysql:localhost")
//  .option("dbtable", "annotations.match_pair")
//  .option("user", "root")
//  .option("password", "")
//  .option("driver", "com.mysql.jdbc.Driver")
//  .save()
//  .jdbc()jdbc:mysql://localhost/annotations?user=root", "match_pair",new Properties())

//.withColumn("recordB", jsonStringify('best_match))
//.withColumn("confidence", 'confidence)
//.withColumn("input_record", 'input_record)
//.withColumn("match_details", 'match_details)
//.withColumn("match_meta", 'match_meta)
//.show()