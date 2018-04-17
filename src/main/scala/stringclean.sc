import java.nio.charset.Charset
import java.text.Normalizer
import info.debatty.java.stringsimilarity.JaroWinkler

val str = "shïnMichèleäöüß 'le 1945"
val US_ASCII = Charset.forName("US-ASCII")
val charsetEncoder = US_ASCII.newEncoder()

//def clean(str:String) = Normalizer.normalize(str, Normalizer.Form.NFKD).toUpperCase().toCharArray.filter(c => c.toInt >=48 && c.toInt <= 90).mkString("")
def clean(str:String) = Normalizer.normalize(str, Normalizer.Form.NFKD).toUpperCase().replaceAll("""[^\w ]""","")

clean(str)

//val adr1 = "Koninging Julianalaan 173"
val adr1 = "+31 (0) 6 45528510"
//val adr2 = "Kon. Julianaln 173-A"
val adr2 = "0645528510"


val jw = new JaroWinkler

clean(adr1.substring(adr1.length-9))
clean(adr2.substring(adr2.length-9))
jw.similarity(adr1, adr2)
jw.similarity(clean(adr1).substring(adr1.length-9), clean(adr2.substring(adr2.length-9)))


