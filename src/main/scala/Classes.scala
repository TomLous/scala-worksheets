
object Classes {

  class HistoryDetail(date: String, val1: Int, val2: Int, changeCode: String) extends Serializable

  class HistoryHeader(numDetailRecords: Int, calcDate: String, historyRecords: List[HistoryDetail]) extends Serializable




}