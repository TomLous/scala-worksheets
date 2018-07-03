import java.io._
import java.text.SimpleDateFormat
import java.util.TimeZone

object ConvertForCQL2 extends App {

  val tenant = "ebay_kleinanzeigen"
  val sdf = new SimpleDateFormat("yyyy-MM-dd")
  sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
  val fileTimeStampMicroSeconds = sdf.parse("2018-06-20").toInstant.toEpochMilli * 1000

  val inputFile = new File("/Users/tlous/Downloads/import/part-00000-b438acac-1d6a-42de-b465-8d59ea55146e-c000.csv")
  //  val inputFile = new File("/Users/tlous/Downloads/import/tmp.txt")
  val outputFile = new File(s"/Users/tlous/Downloads/import/$tenant-2.cql")

  val fileSDF = new SimpleDateFormat("yyyy-MM-dd")
  fileSDF.setTimeZone(TimeZone.getTimeZone("UTC"))

  val writer = new BufferedOutputStream(new FileOutputStream(outputFile, false))
  val reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)))

  var counter = 0

  try {

    writer.write(s"CONSISTENCY LOCAL_QUORUM;\n".getBytes)
    Stream.continually(reader.readLine()).drop(1).takeWhile(_ != null).foreach(
      line => {
        val parts = line.split(",").toList
        val vid = parts(0)
        val epochMilli = fileSDF.parse(parts(1)).toInstant.toEpochMilli

        val q = s"INSERT INTO $tenant.user (visitor_id, last_active_epoch_millis) VALUES ('u$vid', $epochMilli) USING  TIMESTAMP $fileTimeStampMicroSeconds;"

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
