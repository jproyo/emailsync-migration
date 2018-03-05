val projectName = "emailsync-migration"

resolvers += Resolver.sonatypeRepo("releases")

lazy val commonSettings = Seq(
  organization := "com.mxhero",
  name := projectName,
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.4"
)

lazy val awsDeps = Seq(
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.190"
)

lazy val couchbaseDeps = Seq(
  "com.couchbase.client" % "java-client" % "2.5.3"
)

lazy val otherDeps = Seq(
  "com.typesafe" % "config" % "1.3.1"
)

lazy val testing = Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test"
)

lazy val dependencies = Seq(
	libraryDependencies ++= awsDeps ++ couchbaseDeps ++ otherDeps ++ testing
)

scalacOptions += "-feature"
coverageEnabled := true

lazy val mainClassName = "com.mxhero.emailsync.migration.App"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(dependencies: _*)
  .settings(Defaults.itSettings: _*)
  .settings(
  	mainClass in Compile := Some(mainClassName)
  )

packMain := Map(projectName -> mainClassName)
packJvmOpts := Map(projectName -> Seq("-Xmx2048m"))
packGenerateWindowsBatFile := true
packJarNameConvention := "default"
packExpandedClasspath := false
