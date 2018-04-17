import scala.util.matching.Regex

val pattern1 = """(\d+(([\.,])\d+)?)(\w*)""".r
val pattern2 = """(\d+)([\.,](\d+))?(\w+)?""".r

val str = "1.0kb"

val pattern2(before, _, after, unit) = "1.0kb"

val pattern2(aa, bb, cc, dd) = "1MB"

val pattern2(aaa, bbb, ccc, ddd) = "1"

val pattern2(aaaa, bbbb, cccc, dddd) = "1,3333"





Option(ccc).getOrElse("?")
Option(dddd).getOrElse("?")
Option(ddd).getOrElse("?")
Option(dd).getOrElse("?")


val lines = List(
  "<tr><td valign=\"top\"><img src=\"/icons/folder.gif\" alt=\"[DIR]\"></td><td><a href=\"bron/\">bron/</a></td><td align=\"right\">2015-04-23 20:08  </td><td align=\"right\">  - </td><td>&nbsp;</td></tr>",
  "<tr><td valign=\"top\"><img src=\"/icons/back.gif\" alt=\"[PARENTDIR]\"></td><td><a href=\"/\">Parent Directory</a></td><td>&nbsp;</td><td align=\"right\">  - </td><td>&nbsp;</td></tr>",
  "<tr><td valign=\"top\"><img src=\"/icons/hand.right.gif\" alt=\"[   ]\"></td><td><a href=\"README\">README</a></td><td align=\"right\">2014-12-21 22:14  </td><td align=\"right\">1.0K</td><td>&nbsp;</td></tr>",
  "<A href=\"../\">../</A>",
  "<A href=\"0.1.1-SNAPSHOT/\">0.1.1-SNAPSHOT/</a>           02-Sep-2016 12:28    -",
  "<a href=\"maven-metadata.xml.md5\">maven-metadata.xml.md5</a>    02-Sep-2016 13:05  32 bytes",
  "<a HREF=\"perceptron-service_2.11-0.1.1-20160902.122851-1-sources.jar.md5\">perceptron-service_2.11-0.1.1-20160902.122851-1-sources.jar.md5</a>    02-Sep-2016 12:28  32 bytes",
  "<a href=\"perceptron-service_2.11-0.1.1-20160902.122851-1-javadoc.jar\">perceptron-service_2.11-0.1.1-20160902.122851-1-javadoc.jar</a>        02-Sep-2016 12:28  529.60 KB"
)

val parsePatt = """(?i).*<a href="?([^"\s]+)"?[^>]+>([^<]+)</a>.*?((\d{4}-\d{2}-\d{2}|\d{1,2}[-\s][a-z]{3,}[-\s]\d{2,4})\s\d{2}:\d{2}(:\d{2})?).*?(-|\d+([\.,]\d+)?\s*(bytes|[a-z]{1,2})).*?""".r


lines map {
    case parsePatt(all@_*) => all mkString " | "
    case _ => "?"
} mkString "\n"
//val parsePatt(s) = line


val parsePatt(fileName, name, timestampStr, _, _ , fileSize, _ , _) = lines(2)


/*
 <tr><td valign="top"><img src="/icons/back.gif" alt="[PARENTDIR]"></td><td><a href="/">Parent Directory</a></td><td>&nbsp;</td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/hand.right.gif" alt="[   ]"></td><td><a href="README">README</a></td><td align="right">2014-12-21 22:14  </td><td align="right">1.0K</td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="app/">app/</a></td><td align="right">2015-11-09 22:35  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="bron/">bron/</a></td><td align="right">2015-04-23 20:08  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="csv/">csv/</a></td><td align="right">2016-09-02 11:22  </td><td align="right">  - </td><td>&nbsp;</td></tr>
<tr><td valign="top"><img src="/icons/folder.gif" alt="[DIR]"></td><td><a href="postgis/">postgis/</a></td><td align="right">2016-09-02 11:15  </td><td align="right">  - </td><td>&nbsp;</td></tr>



<a href="../">../</a>
<a href="0.1.1-SNAPSHOT/">0.1.1-SNAPSHOT/</a>           02-Sep-2016 12:28    -
<a href="maven-metadata.xml">maven-metadata.xml</a>        02-Sep-2016 13:05  366 bytes
<a href="maven-metadata.xml.md5">maven-metadata.xml.md5</a>    02-Sep-2016 13:05  32 bytes
<a href="maven-metadata.xml.sha1">maven-metadata.xml.sha1</a>   02-Sep-2016 13:05  40 bytes


<a href="perceptron-service_2.11-0.1.1-20160902.122851-1-javadoc.jar">perceptron-service_2.11-0.1.1-20160902.122851-1-javadoc.jar</a>        02-Sep-2016 12:28  529.60 KB
<a href="perceptron-service_2.11-0.1.1-20160902.122851-1-javadoc.jar.md5">perceptron-service_2.11-0.1.1-20160902.122851-1-javadoc.jar.md5</a>    02-Sep-2016 12:28  32 bytes
<a href="perceptron-service_2.11-0.1.1-20160902.122851-1-javadoc.jar.sha1">perceptron-service_2.11-0.1.1-20160902.122851-1-javadoc.jar.sha1</a>   02-Sep-2016 12:28  40 bytes
<a href="perceptron-service_2.11-0.1.1-20160902.122851-1-sources.jar">perceptron-service_2.11-0.1.1-20160902.122851-1-sources.jar</a>        02-Sep-2016 12:28  23.43 KB
<a href="perceptron-service_2.11-0.1.1-20160902.122851-1-sources.jar.md5">perceptron-service_2.11-0.1.1-20160902.122851-1-sources.jar.md5</a>    02-Sep-2016 12:28  32 bytes
<a href="perceptron-service_2.11-0.1.1-20160902.122851-1-sources.jar.sha1">perceptron-service_2.11-0.1.1-20160902.122851-1-sources.jar.sha1</a>   02-Sep-2016 12:28  40 bytes
<a href="perceptron-service_2.11-0.1.1-20160902.122
     */








