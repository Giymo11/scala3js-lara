import sbt.Keys._

val scala = "3.0.1"
val scalajs = "1.7.0"

val zio = "1.0.11"
val zhttp = "1.0.0.0-RC17"
val osLib = "0.7.8"
val tapir = "0.19.0-M7"
val sttpClient3 = "3.3.13"
val laminar = "0.13.1"
val airstream = "0.13.0"

val scalaJsDom = "1.1.0"
val scalaJavaTime = "2.3.0"

val uuid = "8.3.1"

val compilerOptions = Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding",
  "utf-8", // Specify character encoding used by source files.
  "-explain-types", // Explain type errors in more detail.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-language:implicitConversions", // Allow definition of implicit functions called views
  // "-Ykind-projector",
  "-rewrite",
  "-new-syntax", // use new syntax for control structures
  "-source",
  "future", // restricts to scala 3.1 featureset
  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Yexplicit-nulls" // experimental, forces you to think about every null
)

ThisBuild / name := "lara"
ThisBuild / organization := "science.wasabi"
ThisBuild / scalaVersion := scala

ThisBuild / evictionErrorLevel := Level.Info
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "cats-effect" % "always"

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure) // this will cross compile
  .in(file("lara/shared"))
  .settings(
    Compile / scalaSource := baseDirectory.value / "src",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapir,
      "dev.zio" %%% "zio" % zio
    ),
    scalacOptions ++= compilerOptions
  )

lazy val sharedJS = shared.js
lazy val sharedJVM = shared.jvm

lazy val backend = project
  .in(file("lara/backend"))
  .dependsOn(sharedJVM)
  .settings(
    Compile / scalaSource := baseDirectory.value / "src",
    libraryDependencies ++= Seq(
      "io.d11" %% "zhttp" % zhttp,
      "com.lihaoyi" %% "os-lib" % osLib,
      "com.softwaremill.sttp.tapir" %% "tapir-zio-http" % tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-redoc" % tapir
    ),
    scalaJSProjects := Seq(frontend),
    Assets / pipelineStages := Seq(scalaJSPipeline)
  )
  .enablePlugins(WebScalaJSBundlerPlugin)

lazy val frontend = project
  .in(file("lara/frontend"))
  .dependsOn(sharedJS)
  .settings(
    Compile / scalaSource := baseDirectory.value / "src",
    libraryDependencies ++= Seq(
      ("org.scala-js" %%% "scalajs-dom" % scalaJsDom).cross(CrossVersion.for3Use2_13),
      ("com.softwaremill.sttp.tapir" %%% "tapir-sttp-client" % tapir).cross(CrossVersion.for3Use2_13),
      ("com.softwaremill.sttp.client3" %%% "core" % sttpClient3).cross(CrossVersion.for3Use2_13),
      ("io.github.cquiroz" %%% "scala-java-time" % scalaJavaTime).cross(CrossVersion.for3Use2_13),
      "com.raquo" %%% "laminar" % laminar
    ),
    useYarn := true,
    Compile / npmDependencies ++= Seq(
      "uuid" -> uuid,
      "@types/uuid" -> uuid
    ),
    scalaJSUseMainModuleInitializer := true // project with a main method, not a library (roughly)
  )
  .enablePlugins(
    ScalaJSBundlerPlugin,
    ScalablyTypedConverterPlugin
  ) // this where we say that frontend compiles to JavaScript
