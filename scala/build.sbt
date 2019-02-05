organization := "com.github.kmizu"
name := "usparse"
version := "0.1"
scalaVersion := "2.12.8"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:implicitConversions")
initialCommands in console += {
  Iterator("com.github.kmizu.usparse._").map("import "+).mkString("\n")
}
