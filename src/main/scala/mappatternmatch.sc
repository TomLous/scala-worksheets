
val x = Map("a"->1, "b"->2)
val y = Map()
y match {
  case m if m.isEmpty => -1
  case  c => c.size
}