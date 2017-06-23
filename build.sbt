import BuildSettings.{buildSettings, appName}
import PublishSettings._
import AssemblySettings._


// lazy val flow = Project("flow", file("flow"), settings = buildSettings ++ publishSettings).settings(mergeSetting)

lazy val root = Project(appName, file("."), settings = buildSettings ++ publishSettings).settings(mergeSetting)


