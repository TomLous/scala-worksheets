import org.apache.spark.sql.{Encoders, SaveMode, SparkSession}
import org.apache.spark.sql.functions._

import scala.annotation.tailrec


val spark = SparkSession.builder()
  .master("local[*]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._


val evalDf = List(
  (12,3,4,"A+B"),
  (32,8,9,"B*C"),
  (56,12,2,"A+B*C")
).toDF("A", "B", "C", "Eqn")



def evalUDF = udf((a:Int, b:Int, c:Int, eqn:String) => {
  val eqnParts = eqn
    .replace("A", a.toString)
    .replace("B", b.toString)
    .replace("C", c.toString)
    .split("""\b""")
    .toList

  val (sum, _) = eqnParts.tail.foldLeft((eqnParts.head.toInt, "")){
    case ((runningTotal, "+"), num) => (runningTotal + num.toInt, "")
    case ((runningTotal, "-"), num) => (runningTotal - num.toInt, "")
    case ((runningTotal, "*"), num) => (runningTotal * num.toInt, "")
    case ((runningTotal, _), op) => (runningTotal, op)
  }

  sum
})

evalDf
  .withColumn("eval", evalUDF('A, 'B, 'C, 'Eqn))
  .show()
/*
A    B    C    Eqn
12   3    4    A+B
32   8    9    B*C
56   12   2    A+B*C
 */