import java.time._
import scalatags.JsDom.all._

case class Person(firstName: String, lastName: String)

case class Rating(n: Int) {
  assert(n <= 5)
  assert(n > 0)
}

val freelancer = Person("Tom", "Lous")

val title = "Big Data Software Engineer"
val subTitle = "Machine Learning Engineer / Data Scientist"

val mainSkills = List(
  "Scala" -> Rating(5),
  "Apache Spark" -> Rating(5),
  "Machine Learning" -> Rating(4),
  "Airflow" -> Rating(3),
  "Kafka" -> Rating(2)

)

val connectVia = List(
  "Mail: tomlous@gmail.com" -> "mailto:tomlous@gmail.com",
  "Twitter" -> "https://twitter.com/tomlous",
  "LinkedIn" -> "https://www.linkedin.com/in/tomlous/",
  "GitHub" -> "https://github.com/TomLous",
  "StackOverflow" -> "https://stackoverflow.com/users/1444286/tom-lous",
  "Medium" -> "https://medium.com/@tomlous",
  "other" -> "https://about.me/tomlous"
)


val availableFrom = LocalDate.of(2020, 10, 1)


// println(s"Hi, my name is ${freelancer.firstName}\nI'm a $title\nMy skills contain:\n"  + mainSkills.foldLeft("")({
//   case ( str, (skill ,rating)) => str + s"\n * $skill: \t" + ("*" * rating.i)
// }) + "\n\nYou may contact me using:" + connectVia.map(s => s"${s._2} [${s._1}]").mkString("\n\n * ", "\n * ", "\n"))

println(div(
  h1(s"Hi, my name is ${freelancer.firstName}"),
  h2(s"I'm a $title"),
  p("""That basicly means that I develop Scala software that run on a Spark cluster, processing huge amounts of data (or tiny for that matter).
  I'm proficient with many tools concerning setting up a big data ingestion & processing pipeline in the cloud. This of course means basics like: git, Jenkins, Ansible, Airflow, Camel and other DevOps. Cloud systems like GCP, AWS and Azure. Working with data & datbases, SQL, MySQL, PostgreSQL, Cloud Spanner, Big Query, MongoDB, Neo4j, ElasticSearch, Cassandra, etc.
  And of course linux and Hadoop.
  But my specialty is writing, deploying and running Scala & Spark.
  """),
  h2(s"I'm actually also a $subTitle"),
  p("""Calling yourself a data scientist is a bit overloaded term, but I create experiments using statistical, machine learning and linear algerbra libraries, testing hypotheses and predicting features.
  Using Python & scikit, Spark & MLlib and sometimes even R I can create all sorts of models that can be tested on (almost) any scale.
  """),
  h3("My main skills are:"),
  ul(
    for((skill, rating) <- mainSkills)
      yield li(div(img(src:= s"https://basvanstratum.nl/wp-content/plugins/universal-star-rating/includes/stars.php?img=06.png&px=2&max=5&rat=${rating.n}"), span(" - "), b(skill)))
  ),
  h3("Contact me via:"),
  ul(
    for((cType, link) <- connectVia)
      yield li(a(href := link, cType))
  ),
  h3("Availability:"),
  if(LocalDate.now().compareTo(availableFrom) >= 0) h1("I'm available") else h4(s"I will be available from $availableFrom")



))