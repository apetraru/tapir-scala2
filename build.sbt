val tapirVersion = "1.2.10"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "tapirdemo",
    version := "0.1.0-SNAPSHOT",
    organization := "demo",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
      "org.http4s" %% "http4s-ember-server" % "0.23.18",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "ch.qos.logback" % "logback-classic" % "1.4.5",
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.15" % Test,
      "com.softwaremill.sttp.client3" %% "circe" % "3.8.13" % Test
    )
  )
)
