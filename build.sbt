import BuildSettings.{buildSettings, appName}
import PublishSettings._
import AssemblySettings._


lazy val macros = project.in(file("macros")).settings(
  scalaVersion := "2.11.11",
  scalacOptions ++= Seq("-deprecation", "-feature"),
  libraryDependencies ++= Seq(
     "org.scala-lang" % "scala-reflect" % scalaVersion.value,
     "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1",
     "org.specs2" %% "specs2" % "2.3.12" % "test"
  )
)

lazy val root = Project(appName, file("."),
  settings = buildSettings ++ publishSettings).settings(mergeSetting).dependsOn(macros)




