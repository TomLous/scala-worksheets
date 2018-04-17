import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.sql.functions._



case class Credit (
  creditability:Double = 0.0,
  balance:Double = 0.0,
  duration:Double = 0.0,
  history:Double = 0.0,
  purpose:Double = 0.0,
  amount:Double = 0.0,
  savings:Double = 0.0,
  employment:Double = 0.0,
  instPercent:Double = 0.0,
  sexMarried:Double = 0.0,
  guarantors:Double = 0.0,
  residenceDuration:Double = 0.0,
  assets:Double = 0.0,
  age:Double = 0.0,
  concCredit:Double = 0.0,
  apartment:Double = 0.0,
  credits:Double = 0.0,
  occupation:Double = 0.0,
  dependents:Double = 0.0,
  hasPhone:Double = 0.0,
  foreign:Double = 0.0
)



//val spark = SparkSession.builder()
//     .master("local[2]")
//     .appName("test")
//    .getOrCreate()
//
//val rdd = spark.sparkContext.textFile("src/main/resources/input.txt")
//  .map(_.split("  "))
//

List(1,2,3,4,5,6,5,7,7).map(_.toDouble)





//rdd.foreach(println)
//val spark = SparkSession.builder()
//     .master("local")
//     .appName("Word Count")
//     .config("spark.some.config.option", "some-value")
//     .getOrCreate()
//
//import spark.implicits._
//val df = spark.read
//  .text("src/main/resources/input.txt")
//
//df.select(explode(split('value, "  "))).show()
//
//df.show()




