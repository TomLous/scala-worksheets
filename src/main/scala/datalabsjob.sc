import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DoubleType

import scala.collection.Map


case class GooglePlacesReview(
                               aspects: Seq[GooglePlacesReviewAspect],
                               author_name: Option[String],
                               author_url: Option[String],
                               language: Option[String],
                               profile_photo_url: Option[String],
                               rating: Option[Long],
                               relative_time_description: Option[String],
                               text: Option[String],
                               time: Option[Long]
                             )


case class GooglePlacesReviewAspect(rating: Long, `type`: String)

//case class GooglePlacesEntry(reviewsx: Int,reviews: Seq[GooglePlacesReview])
case class GooglePlacesEntry(reviewsx: Int)


val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test2")
  .config("spark.driver.allowMultipleContexts", "true")
  .getOrCreate()

import spark.implicits._

org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)

val dataFrame = spark.read.json("../datalabs-job/src/test/resources/facebook/facebook-places_results_detail_10k_20161011130202.json")

//def checkReview = udf((x:Seq[Any]) => x)

dataFrame
    .select('id, $"location.latitude".as("lat").cast(DoubleType), $"location.longitude".as("lon"))
   .filter(length('lat) === 0 || length('lon) === 0)
  .show(false)
