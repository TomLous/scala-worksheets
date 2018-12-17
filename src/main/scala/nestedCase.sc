import java.security.cert.PKIXRevocationChecker.Option

val s1 = Some(1)
val s2 = Some(2)
val s3 = Some(3)
val n = None

for {
  _ <- n.toRight(0).left
  _ <- s3.toRight(0).left
  _ <- n.toRight(0).left
  _ <- s1.toRight(0).left
  c <- n.toRight(0).left
} yield c
