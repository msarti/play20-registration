import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "signin-sample"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
	"it.sartimarco" %% "play20-signin" % "1.0-SNAPSHOT"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
