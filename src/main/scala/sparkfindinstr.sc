import java.io._
import java.nio.{ByteBuffer, CharBuffer}

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.execution.streaming.FileStreamSource
import org.apache.spark.sql.streaming.OutputMode

import scala.collection.mutable.ArrayBuffer
import scala.reflect.io.File

val spark = SparkSession.builder()
  .master("local[3]")
  .appName("test")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._


val fileStreamDf = spark.readStream
  .text("src/main/resources/stream/")

spark.readStream.load()

val query = fileStreamDf.writeStream
  .format("console")
  .outputMode(OutputMode.Append()).start()

query.processAllAvailable
query.stop

//
//val fileIn = "src/main/resources/testString.txt"
//val fileOut = "src/main/resources/genomeStringFormatedForSpark.txt"
//val reader = new FileInputStream(fileIn)
//val writer = new BufferedOutputStream(new FileOutputStream(fileOut))
//
//val recordSize = 9
//val maxSearchLength = 3
//
//val bytes = Array.fill[Byte](recordSize)(0)
//val prefix = Array.fill[Byte](maxSearchLength)(' ')
//
//Stream
//  .continually((reader.read(bytes),bytes))
//  .takeWhile(_._1 != -1)
//  .foreach{
//    case (_, buffer) =>   {
//      writer.write(prefix ++ buffer :+ '\n'.toByte)
//      Array.copy(buffer.toList.takeRight(maxSearchLength).toArray,0,prefix,0,maxSearchLength)
//    }}
//
//writer.close
//reader.close









/* create huge ass string */
//val fileIn = "src/main/resources/genome.txt"
//val fileOut = "src/main/resources/genomeString.txt"
//val reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileIn)))
//val writer = new BufferedOutputStream(new FileOutputStream(fileOut))
//
//
//try {
//  Stream.continually(reader.readLine())
//    .takeWhile(_ != null)
//    .foreach(line => writer.write(s"$line".getBytes))
//} catch {
//  case e: Exception => println(e.getMessage)
//} finally {
//  println("Close")
//  writer.close()
//  reader.close()
//}