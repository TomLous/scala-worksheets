import java.io._

val inputFile = new File("/Users/tlous/Downloads/import/2018-05-04_LM_User_Last_Activity_Date.txt")
val outputFile = new File("/Users/tlous/Downloads/import/output.csv")

val writer = new BufferedOutputStream(new FileOutputStream(outputFile, true))
val reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)))

try {
  Stream.continually(reader.readLine())
    .map(line => {
      if (line != null)
        writer.write(s"Tom$line\n".getBytes)

      line
    })
    .take(100)
//    .takeWhile(_ != null)

} catch {
  case e: Exception => println(e.getMessage)
} finally {
  writer.close()
  reader.close()
}

