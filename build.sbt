import sbt.file

lazy val preprocessors = (project in file("preprocessors"))
  .settings(
    name := "preprocessors",
    libraryDependencies += "software.amazon.smithy" % "smithy-build" % smithy4s.codegen.BuildInfo.smithyVersion
  )

lazy val root = (project in file("."))
  .enablePlugins(Smithy4sCodegenPlugin)
  .settings(
    organization := "com.kmalik",
    version := "0.0.1-SNAPSHOT",
    name := "hello-smithy",
    scalaVersion := "2.13.12",
    crossScalaVersions := Seq("2.13.12", "2.12.18"),
    libraryDependencies ++= Seq(
      "software.amazon.smithy" % "smithy-model" % "1.42.0",
      "com.disneystreaming.smithy4s" %% "smithy4s-core" % smithy4sVersion.value,
      "org.typelevel" %% "cats-effect" % "3.5.3",
    ),
    Compile / smithy4sModelTransformers += "TenantAgnosticTransformation",
    Compile / smithy4sAllDependenciesAsJars += (preprocessors / Compile / packageBin).value
  )
