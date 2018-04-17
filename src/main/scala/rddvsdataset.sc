import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

case class DatlinqRecord(id: String, typeName: String)
case class LSH(hash: String, record: DatlinqRecord)

val spark = SparkSession.builder()
  .master("local[2]")
  .appName("test")
  .getOrCreate()

import spark.implicits._
org.apache.spark.sql.catalyst.encoders.OuterScopes.addOuterScope(this)

val input = List(
  LSH("hashA", DatlinqRecord("001", "Type1")),
  LSH("hashA", DatlinqRecord("002", "Type2")),
  LSH("hashA", DatlinqRecord("003", "Type3")),
  LSH("hashB", DatlinqRecord("004", "Type1")),
  LSH("hashB", DatlinqRecord("005", "Type2")),
  LSH("hashB", DatlinqRecord("006", "Type3")),
  LSH("hashC", DatlinqRecord("007", "Type1")),
  LSH("hashC", DatlinqRecord("008", "Type2")),
  LSH("hashC", DatlinqRecord("009", "Type3")),
  LSH("hashD", DatlinqRecord("010", "Type1")),
  LSH("hashD", DatlinqRecord("011", "Type2")),
  LSH("hashD", DatlinqRecord("012", "Type3"))
)

//def choose(datlinqRecord: DatlinqRecord):Boolean = datlinqRecord.typeName == "Type1"




val dataset = input.toDS()

val res = dataset
  .select(
    'hash,
    when($"record.typeName" === "Type1", 'record).as("left"),
    when($"record.typeName" =!= "Type1", 'record).as("right")
  )
  .groupBy('hash)
  .agg(
    struct(
      collect_set('left),
      collect_set('right)
    ).as("agg")
  )
  .as[(String, (Seq[DatlinqRecord], Seq[DatlinqRecord]))]


res.show(false)
res.printSchema()
