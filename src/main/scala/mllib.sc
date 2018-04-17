import org.apache.spark.ml.feature.Word2Vec
import org.apache.spark.sql.SparkSession
import info.debatty.java.stringsimilarity.QGram
import scala.collection.JavaConversions._
import collection.JavaConverters._

val spark = SparkSession.builder()
     .master("local[2]")
     .appName("test")
    .getOrCreate()



case class MatchRecord(id: String, text: String)
case class MatchRecord2(id: String, text: String, qg:Map[String,Int])

class Shingle extends Serializable{
  val qg = new QGram(2)

  def profile(str: String):Map[String, Int] =
    qg.getProfile(str).asScala.map(r => (r._1, r._2.toInt)).toMap
}


import spark.implicits._
org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)



val documentDF = spark.createDataFrame(List(
  ("ac974535-7d93-46ef-89e4-49840900475e","JIMMYS BROODJE SCHEVENINGSEVEER 1 2563HB DEN HAAG NL 070 360 1152 HTTPWWWJIMMYSBROODJENL"),
  ("19451821-48da-4cbd-8b77-6289001cc54c","ATELIER ANART SCHEVENINGSEVEER 19 2514HB DEN HAAG NL 06 48068688"),
  ("e1a333f8-def4-41ac-b2e2-68cb15bc44ad","MERCADO SCHEVENINGSEVEER 13 2514HB DEN HAAG NL 070 361 7123 HTTPWWWMERCADOSENL"),
  ("97367443-813a-4c8a-85aa-0f99eb893586","JUST GRAPES SCHEVENINGSEVEER 17 2514HB DEN HAAG NL 06 41218299 HTTPJUSTGRAPESNL"),
  ("5f364369-0bc9-4a08-b850-5748978761c7","A OFMAN BEHEER BV SCHEVENINGSEVEER 15 2514HB DEN HAAG NL 06 53265660"),
  ("89b383f0-9cfc-49c9-bfc0-244dcc08bbeb","PONTMEIJER SCHEVENINGEN SCHEVENINGSEVEER 19 2514HB DEN HAAG NL 06 53619211"),
  ("f1b11c11-d860-4e46-be6e-799aba4cdd39","MERCADO SCHEVENINGSEVEER 13 2514HB DEN HAAG NL 070 361 7123"),
  ("ad02ca5e-4548-43fd-a25f-0cc0801c4c6a","PRINS TAVEERNE SCHEVENINGSEVEER 1 2514HB DEN HAAG NL 070 350 5210"),
  ("a00cdf4e-f20b-4a69-9404-474e9ef6a147","JIMMYS BROODJE SCHEVENINGSEVEER 1 2514HB DEN HAAG NL 070 360 1152 HTTPWWWJIMMYSBROODJENL"),
  ("1ed8fa93-f7e0-4113-b9b3-65ca0570dfec","PONTMEYER SCHEVENINGSEVEER 19 2514HB DEN HAAG NL 070 365 4940"),
  ("6c93350a-22d7-4814-b9a2-838380cbe6c5","PRINSENKELDER SNACKBAR SCHEVENINGSEVEER 11 2514HB DEN HAAG NL 070 360 1776"),
  ("9ce92ffc-6999-40f2-9622-c547c04fc53b","BROODJESZAAK JIMMYS BROODJE SCHEVENINGSEVEER 1 2514HB SGRAVENHAGE NL 0703601152 WWWJIMMYSBROODJENL"),
  ("2920a950-4760-4b30-8774-270c1293ec80","CAFETARIA DE PRINSENKELDER SCHEVENINGSEVEER 11 2514HB SGRAVENHAGE NL 0703601776"),
  ("279617db-dff1-41f6-b3e9-010ded25ab1e","MERCADO AVONDWINKEL SCHEVENINGSEVEER 13 2514HB SGRAVENHAGE NL 0703617123")
)).toDF("id","text")
  .as[MatchRecord]
  .map(r => {
    MatchRecord2(r.id, r.text, new Shingle().profile(r.text))
  })

.show()
//// Input data: Each row is a bag of words from a sentence or document.
//val documentDF = spark.createDataFrame(Seq(
//  "Hi I heard about Spark".split(" "),
//  "I wish Java could use case classes".split(" "),
//  "Logistic regression models are neat".split(" ")
//).map(Tuple1.apply)).toDF("text")
//
//// Learn a mapping from words to Vectors.
//val word2Vec = new Word2Vec()
//  .setInputCol("text")
//  .setOutputCol("result")
//  .setVectorSize(3)
//  .setMinCount(0)
//val model = word2Vec.fit(documentDF)
//val result = model.transform(documentDF)
//result.select("result").take(3).foreach(println)
//import org.apache.spark.ml.feature.NGram
//
//val wordDataFrame = spark.createDataFrame(Seq(
//  (0, Array("Hi", "I", "heard", "about", "Spark")),
//  (1, Array("I", "wish", "Java", "could", "use", "case", "classes")),
//  (2, Array("Logistic", "regression", "models", "are", "neat"))
//)).toDF("id", "words")
//
//val ngram = new NGram().setN(2).setInputCol("words").setOutputCol("ngrams")
//
//val ngramDataFrame = ngram.transform(wordDataFrame)
//ngramDataFrame.select("ngrams").show(false)