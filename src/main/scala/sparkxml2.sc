import java.io._

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

// Using databricks api save as 1 big xml file (instead of many parts, using repartition)
// You don't have to use repartition, but each part-xxx file will wrap contents in the root tag, making it harder to concat later.
// And TBH it really doesn't matter that Spark is doing the merging here, since the combining of data is already on the master node only
bookFrame
  .repartition(1)
  .write
  .format("com.databricks.spark.xml")
  .option("rootTag", "books")
  .option("rowTag", "book")
  .mode(SaveMode.Overwrite)
  .save("/tmp/books/") // store to temp location

// Same for reviews
reviewFrame
  .repartition(1)
  .write
  .format("com.databricks.spark.xml")
  .option("rootTag", "reviews")
  .option("rowTag", "review")
  .mode(SaveMode.Overwrite)
  .save("/tmp/review") // store to temp location


def concatFiles(path: String): List[String] = {
  new File(path)
    .listFiles
    .filter(
      _.getName.startsWith("part") // get all part-xxx files only (should be only 1)
    )
    .flatMap(file => Source.fromFile(file.getAbsolutePath).getLines())
    .map("    " + _) // prefix with spaces to allow for new root level xml
    .toList
}


def outputConcatFiles(path: String, outputFile: File): Unit = {
  new File(path)
    .listFiles
    .filter(
      _.getName.startsWith("part") // get all part-xxx files only (should be only 1)
    )
    .foreach(file => {
      val writer = new BufferedOutputStream(new FileOutputStream(outputFile, true))
      val reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))

      try {
        Stream.continually(reader.readLine())
          .map(line => {
            if (line != null)
              writer.write(s"    $line\n".getBytes)

            line
          })
          .takeWhile(_ != null)

      } catch {
        case e: Exception => println(e.getMessage)
      } finally {
        writer.close()
        reader.close()
      }
    })
}

val outputFile = new File("/tmp/target2.xml")
new PrintWriter(outputFile) { write("<xml>\n<library>\n"); close}
outputConcatFiles("/tmp/books/", outputFile)
outputConcatFiles("/tmp/review/", outputFile)
new PrintWriter(new FileOutputStream(outputFile, true)) { append("</library>"); close}


//val lines = List("<xml>","<library>") ++ concatFiles("/tmp/books/") ++ concatFiles("/tmp/review/") ++ List("</library>")
//new PrintWriter("/tmp/target.xml"){
//  write(lines.mkString("\n"))
//  close
//}
