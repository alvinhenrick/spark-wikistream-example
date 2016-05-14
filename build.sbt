name := "spark-wikistream-example"

version := "0.0.1"

scalaVersion := "2.10.5"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

val sparkVersion = "1.6.1"

// additional libraries
libraryDependencies ++= Seq(

  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-hive" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming-kafka" % sparkVersion,

  "org.pircbotx" % "pircbotx" % "2.0",
  "org.apache.kafka" %% "kafka" % "0.9.0.1",
  "org.apache.kafka" % "kafka-clients" % "0.9.0.1",
  "io.spray" %% "spray-json" % "1.3.2"

  //"org.apache.hadoop" % "hadoop-common" % "2.6.0" % "provided"

)
ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.startsWith("META-INF") => MergeStrategy.discard
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList("org", "apache", xs@_*) => MergeStrategy.first
  case "about.html" => MergeStrategy.rename
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}

