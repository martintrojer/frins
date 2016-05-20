name := "frins"

organization := "io.github.martintrojer"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.2"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "io.github.martintrojer" % "atom-scala_2.10" % "0.1-SNAPSHOT"

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
