import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature._
import org.apache.spark.ml.linalg.{SparseVector, Vectors}
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions._

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._


val regexTransformedLabel = Seq(
  (1, "A B C D E"),
  (1, "B C D"),
  (1, "B C D E"),
  (1, "B C D F"),
  (1, "A B C"),
  (1, "B C E F G")
).toDF("n", "regexTransformedColumn")



// Create the Tokenizer step
val tokenizer = new Tokenizer()
  .setInputCol("regexTransformedColumn")
  .setOutputCol("words")

// Create CountVecoritzer for label vocab
val countVectorizer = new CountVectorizer()
  .setInputCol(tokenizer.getOutputCol)
  .setOutputCol("rawFeatures")
  .setMinDF(1)

// Combine into count vectorizer pipeline
val cvPipeline = new Pipeline()
  .setStages(Array(tokenizer, countVectorizer))

// Create pipeline for token & count vectorizer (TF)
val pipelineModel = cvPipeline.fit(regexTransformedLabel)

// Extract vocabulary
val vocabulary = pipelineModel.stages.last.asInstanceOf[CountVectorizerModel].vocabulary

// Transform the dataset to TF dataset
val termFrequencyData = pipelineModel.transform(regexTransformedLabel)

// Create IDF
val idf = new IDF().setInputCol(countVectorizer.getOutputCol).setOutputCol("features")

// Fit the IDF on the TF data
val lrModel = idf.fit(termFrequencyData)

// Tranform the TF Data into TF/IDF data
val lrOutput = lrModel.transform(termFrequencyData)


def removeLeastUsed(treshold: Double) = udf((features: SparseVector) => {
  (features.indices zip features.values) filter(_._2 < treshold) map {
    case (index, _) => vocabulary(index)
  }
})

//lrOutput.select()

vocabulary.foreach(println)

lrOutput
  .select(
    'regexTransformedColumn,
    'features,
    removeLeastUsed(0.6)('features).as("mostUsedWords")
  )
  .show(false)
lrOutput.printSchema()


