import java.io.{File, PrintWriter}

import org.apache.spark.sql.{SaveMode, SparkSession}

import scala.io.Source

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

/* Some code to test */
case class Book(author: String)
case class Review(id: Int)
case class BookReview(books:List[Book], reviews: List[Review])
org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)


val bookFrame = List(
  Book("book1"),
  Book("book2"),
  Book("book3"),
  Book("book4"),
  Book("book5")
).toDS()

val reviewFrame = List(
  Review(1),
  Review(2),
  Review(3),
  Review(4)
).toDS()

/* End test code **/


val bookReview = BookReview(
  bookFrame.collect().toList,
  reviewFrame.collect().toList)

List(bookReview)
  .toDS()
  .write
  .format("com.databricks.spark.xml")
  .option("rootTag", "xml")
  .option("rowTag", "library")
  .mode(SaveMode.Overwrite)
  .save("/tmp/target/") // store to temp location



