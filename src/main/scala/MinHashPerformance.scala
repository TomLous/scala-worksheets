import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature._
import org.apache.spark.ml.param.{IntParam, Param}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object MinHashPerformance  extends App {

  val logger = org.apache.log4j.LogManager.getLogger(getClass.getName.init)

  // config
  val shingleSize = 2
  val numFeatures = 100000
  val numHashTables = 2

  // Pipeline
//  val tokenizer = new ShingleTokenizer()
//    .setShingleSize(shingleSize)
//    .setInputCol("location")
//    .setOutputCol("parts")

  val tokenizer = new Tokenizer()
    .setInputCol("location")
    .setOutputCol("parts")

  val hashingTF = new HashingTF()
    .setNumFeatures(numFeatures)
    .setInputCol(tokenizer.getOutputCol)
    .setOutputCol("rawFeatures")

  val minHash = new MinHashLSH()
    .setNumHashTables(numHashTables)
    .setInputCol(hashingTF.getOutputCol)
    .setOutputCol("hashes")

  val pipeline = new Pipeline()
    .setStages(Array(tokenizer, hashingTF, minHash))

  // test tokenizer
//  val testTokenize = tokenizer.asInstanceOf[ShingleTokenizer].createTransformFunc
//  println(testTokenize("Denville|07834|NJ|New Jersey"))



  // Spark
  logger.info("Start")
  val spark = SparkSession.builder().master("local[*]").appName("test").config("spark.ui.enabled", "true").getOrCreate()
  import spark.implicits._

  logger.info("Load cities")
  val cities = spark.read.parquet("/tmp/cities_exploded.parquet")
  logger.info(s"cities Info: # records ${cities.count}, # partitions ${cities.rdd.partitions.length}")


  logger.info("Load checkLocations")
  val checkLocations = spark.read.parquet("/tmp/exploded.parquet")
  logger.info(s"checkLocations Info: # records ${checkLocations.count}, # partitions ${checkLocations.rdd.partitions.length}")

  // Train the model
  logger.info("Train LSH model")
  val model = pipeline.fit(cities)


  // Transform the data
  logger.info("Transform cities")
  val citiesTransformed = model.transform(cities)
  logger.info(s"citiesTransformed Info: # records ${citiesTransformed.count}, # partitions ${citiesTransformed.rdd.partitions.length}")
  citiesTransformed.show(3)
  citiesTransformed.printSchema()

  logger.info("Transform checks")
  val checkLocationsTransformed = model.transform(checkLocations)
  logger.info(s"checkLocationsTransformed Info: # records ${checkLocationsTransformed.count}, # partitions ${checkLocationsTransformed.rdd.partitions.length}")
  checkLocationsTransformed.show(3)
  checkLocationsTransformed.printSchema()

  // Get the minhash part
  val minHashLSHModel = model.stages.last.asInstanceOf[MinHashLSHModel]

  logger.info("approxSimilarityJoin")
  val joined = minHashLSHModel.approxSimilarityJoin(checkLocationsTransformed, citiesTransformed,0.6)

  logger.info(s"Joined info: # records ${joined.count}, # partitions ${joined.rdd.partitions.length}")
  joined.show(3)
  joined.printSchema()

  logger.info("End")

  /**
    * Custom Shingle Tokenizer.
    * Needed shingling to account for typo's & spelling mistakes.
    * But I didn't want to create non-informative shingles for information parts that are independent:
    *
    * eg. "Denville|07834|NJ|New Jersey" will become
    * ArraySeq( d, de, en, nv, vi, il, ll, le, e ,  0, 07, 78, 83, 34, 4 ,  n, nj, j ,  n, ne, ew, w ,  j, je, er, rs, se, ey, y )
    *
    */
  class ShingleTokenizer(override val uid: String) extends Tokenizer {
    final val defaultShingleSize = 2
    final val defaultSplitPartChar = '|'
    final val shingleSize = new IntParam(this, "shingleSize", "k-shingle size")
    final val splitPartChar = new Param[Char](this, "splitChar", "char to split the string on")

    def this() = this(Identifiable.randomUID("shingleTokenizer"))

    def setShingleSize(value: Int): ShingleTokenizer = set(shingleSize, value).asInstanceOf[ShingleTokenizer]

    def setSplitChar(value: Char): ShingleTokenizer = set(splitPartChar, value).asInstanceOf[ShingleTokenizer]

    override def createTransformFunc: String => Seq[String] = {
      _
        .toLowerCase
        .split(get[Char](splitPartChar).getOrElse(defaultSplitPartChar))
        .flatMap(part =>
          s" $part "
            .replaceAll("""[^a-z0-9]+""", " ")
            .toCharArray
            .sliding(get[Int](shingleSize).getOrElse(defaultShingleSize))
            .map(_.mkString)
            .toIndexedSeq
        )
    }

    // Used to show off m
    //    def publicCreateTransformFunc = createTransformFunc
  }
}
