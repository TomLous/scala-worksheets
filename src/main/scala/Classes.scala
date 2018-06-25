
object Classes {

  class HistoryDetail(date: String, val1: Int, val2: Int, changeCode: String) extends Serializable

  class HistoryHeader(numDetailRecords: Int, calcDate: String, historyRecords: List[HistoryDetail]) extends Serializable


  case class Measure(val1: Option[String], val2:Option[Double], val3:Option[Double], val4:Option[Double], val5:Option[String], val6:Option[String], val7:Option[Int])

}