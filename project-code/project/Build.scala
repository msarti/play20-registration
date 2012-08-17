import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play20-signin"
    val appVersion      = "1.0-SNAPSHOT"

    crossScalaVersions := Seq("2.9.1", "2.10.0")

    val appDependencies = Seq(
      "com.typesafe" %% "play-plugins-mailer" % "2.0.2"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
        organization := "it.sartimarco"
    )

}
