
name := "listing-images-importer"

//version := "1.0"
version := Option(System.getenv("GO_PIPELINE_LABEL")).getOrElse("1.0")

scalaVersion := "2.12.2"

//mainClass in (Compile, run) := Some("com.example.producer.PlainSourceConsumerMain")


lazy val akkaVersion = "2.5.3"
lazy val akkaHttpVersion = "10.0.9"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "0.16"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaVersion  % Test

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.2"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "net.logstash.logback" % "logstash-logback-encoder" % "4.11"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.3" % "test"

libraryDependencies += "joda-time" % "joda-time" % "2.9.9"

libraryDependencies += "com.pauldijou" %% "jwt-core" % "0.14.0"

assemblyJarName in assembly := name.value + "-" + version.value + ".jar"


