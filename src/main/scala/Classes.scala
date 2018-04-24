/**
  * Created by Tom Lous on 23/04/2018.
  * Copyright © 2018 Datlinq B.V..
  */
object Classes {

  class HistoryDetail(date: String, val1: Int, val2: Int, changeCode: String) extends Serializable

  class HistoryHeader(numDetailRecords: Int, calcDate: String, historyRecords: List[HistoryDetail]) extends Serializable




}