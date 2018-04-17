
val map1 = Map("a"->Some(1), "b"->Some(3), "c"->None, "d"->None, "h"->Some(34), "z"->None)
val map2 = Map("a"->None, "b"->Some(11),  "c"->Some(22), "d"->None, "e"->Some(12), "f"->None)


(map1.toList ++ map2.toList)
  .groupBy(_._1)
  .mapValues(_.flatMap(_._2).headOption)
//val x  = (m1.toList ++ m2.toList).foldLeft(Map.empty[String, Option[Int]])

//val merged = (m1 /: m2) {
//  case (map, (k,v)) => map
//}

(map2 ++ map1).map{
  case (k,None) => k->map2.get(k).flatten
  case z => z
}

//(map1.keys ++ map2.keys).toSet.foldLeft(List.empty[(String, Option[Int])])  {
//  case(map, k) => k ->  :: map
//}.toMap

Some(13) ++ None

Some(13) ++ Some(14)

None ++ Some(13)

(map1.keys ++ map2.keys).map(k => k -> (map1.get(k) ++ map2.get(k)).flatten.headOption).toMap