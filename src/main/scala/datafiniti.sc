import com.datlinq.datafiniti._
import com.datlinq.datafiniti.config.DatafinitiAPIFormats._
import com.datlinq.datafiniti.config.DatafinitiAPIViews._

import org.json4s._
import org.json4s.native.JsonMethods._

import org.json4s.JsonAST.JNothing
import scala.concurrent.Await
import scala.concurrent.duration.Duration

import scala.concurrent.ExecutionContext.Implicits.global


val query = """sourceURLs:*just-eat.co.uk*"""
//
val apiKey = "zxp6l3dclwd3xp4g2b6ioytbzcyxnfag"
val apiv3 = DatafinitiAPIv3(apiKey)

//val futureEither = apiv3.query(
//  apiView = BusinessesAllNested,
//  query = Some(query),
//  numberOfRecords = Some(10),
//  download = Some(false),
//  format = JSON)
//
//val result = Await.result(futureEither.value, Duration.Inf)
//
//val json = result.getOrElse(JNothing)
//
//
//pretty(render(json))


val futureEither2 = apiv3.downloadLinks(
  apiView = BusinessesAllNested,
  query = Some(query),
  format = CSV
)

val result2 = Await.result(futureEither2.value, Duration.Inf)

val links:List[String] = result2.getOrElse(Nil)

links