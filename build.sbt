lazy val root = (project in file(".")).
  settings(
    name := "taxitracking",
    version := "1.0",
    scalaVersion := "2.11.6",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.3.11",
      "com.typesafe.akka" %% "akka-testkit" % "2.3.11",
      "com.typesafe.akka" %% "akka-cluster" % "2.3.11",
      "com.typesafe.akka" %% "akka-slf4j" % "2.3.11",
      "org.scalatest" %% "scalatest" % "2.2.4" % "test",
      "ch.qos.logback" % "logback-classic" % "1.1.3"))

