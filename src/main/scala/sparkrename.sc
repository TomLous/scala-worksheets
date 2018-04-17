import java.sql.Date

import org.apache.spark.sql.functions._
import org.apache.spark.sql._

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()


import spark.implicits._

val policy1 = ("10931375", "TEMP", "US")
val policy2 = ("1328904", "TEAM", "US")
var policy = Seq(policy1, policy2).toDF("ID", "Source", "Country")

policy.show()

object Rules {
  val colMapping = Map("ID" -> "NEW_ID",
    "Source" -> "NEW_Source")





  def renameColumns(ds: Dataset[Row]): DataFrame = {
    colMapping.foldLeft(ds){
      case (d, (oldName, newName)) => {
        d.withColumnRenamed(oldName, newName)
      }
    }
  }
}

import Rules._
renameColumns(policy).show()