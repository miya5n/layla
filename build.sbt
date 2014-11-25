name := """layla"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.33",
  "org.scalikejdbc" % "scalikejdbc-play-plugin_2.11" % "2.3.2",
  "org.specs2" % "specs2-core_2.11" % "2.4.9-scalaz-7.0.6",
  "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.1"
)
