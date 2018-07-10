import java.io._
import java.text.SimpleDateFormat
import java.util.TimeZone

object ConvertForCQL extends App {

  val tenant = "ebay_kleinanzeigen"
  val sdf = new SimpleDateFormat("yyyy-MM-dd")
  sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
  val fileTimeStamp = sdf.parse("2018-05-08").toInstant.getEpochSecond


  val inputFile = new File("/Users/tlous/Downloads/import/2018-05-04_LM_User_Last_Activity_Date.txt")
  //  val inputFile = new File("/Users/tlous/Downloads/import/tmp.txt")
  val outputFile = new File("/Users/tlous/Downloads/import/complete-output.txt")

  val fileSDF = new SimpleDateFormat("M/d/yyyy")
  fileSDF.setTimeZone(TimeZone.getTimeZone("UTC"))

  val writer = new BufferedOutputStream(new FileOutputStream(outputFile, false))
  val reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)))

  var counter = 0

  try {
    Stream.continually(reader.readLine()).drop(1).takeWhile(_ != null).foreach(
      line => {
        val parts = line.split("\t").toList
        val uid = parts(1)
        val epoch = fileSDF.parse(parts(2)).toInstant.toEpochMilli

        val q = s"INSERT INTO $tenant.user (visitor_id, last_active_epoch_millis) VALUES ('$uid', $epoch) USING  TIMESTAMP $fileTimeStamp;"

        counter = counter + 1
        if (counter % 10000 == 0)
          println(counter)
        writer.write(s"$q\n".getBytes)
      }
    )


  } catch {
    case e: Exception => println(e.getMessage)
  } finally {
    writer.close()
    reader.close()
  }

  println(s"$counter: Done")


}
