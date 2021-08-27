import $ivy.`com.lihaoyi::mill-contrib-docker:$MILL_VERSION`
import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

import mill._
import mill.api.PathRef
import mill.define.Task
import mill.scalajslib._
import mill.scalalib._
import mill.scalalib.scalafmt._

import contrib.docker.DockerModule

import ammonite.ops._
import $file.webpack
import webpack.ScalaJSWebpackModule

object lara extends Module {
  object Versions {
    val scala = "3.0.1"
    val scalajs = "1.7.0"

    val zio = "1.0.11"
    val zhttp = "1.0.0.0-RC17"
    val osLib = "0.7.8"
    val tapir = "0.19.0-M7"
    val sttpClient3 = "3.3.13"
    val laminar = "0.13.1"
    val airstream = "0.13.0"

    val uuid = "8.3.2"

    // has to be this way because sjsdom is not published for scala 3
    val sjsWorkaround = "_sjs1_2.13"

    val sharedDeps = Agg(
      ivy"com.softwaremill.sttp.tapir::tapir-core:$tapir",
      ivy"dev.zio::zio::$zio"
    )

    val jvmDeps = Agg(
      ivy"io.d11::zhttp:$zhttp",
      ivy"com.lihaoyi::os-lib:$osLib",
      ivy"com.softwaremill.sttp.tapir::tapir-zio-http:$tapir",
      ivy"com.softwaremill.sttp.tapir::tapir-redoc:$tapir"
    )

    val jsDeps = Agg(
      ivy"org.scala-js:scalajs-dom$sjsWorkaround:1.1.0",
      ivy"com.softwaremill.sttp.client3::core::$sttpClient3",
      ivy"com.softwaremill.sttp.tapir:tapir-sttp-client$sjsWorkaround:$tapir",
      ivy"io.github.cquiroz::scala-java-time::2.3.0",
      ivy"com.raquo::laminar::$laminar"
    )

    val npmDeps = Agg(
      "uuid" -> uuid
    )
  }

  trait Common extends ScalaModule {
    override def scalaVersion = Versions.scala

    override def ivyDeps = super.ivyDeps() ++ Versions.sharedDeps

    override def sources = T.sources(
      millSourcePath / "src",
      millSourcePath / os.up / "shared" / "src"
    )

    override def scalacOptions = Seq(
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
  }

  object shared extends Common

  object frontend extends Common with ScalaJSWebpackModule with ScalafmtModule {
    override def scalaJSVersion = Versions.scalajs

    override def ivyDeps = super.ivyDeps() ++ Versions.jsDeps
    override def npmDeps = super.npmDeps() ++ Versions.npmDeps

    override def mainClass = Some("science.wasabi.lara.frontend.Main")

    // fastopt or fullopt
    override def optimizeJs = false
  }

  object backend extends Common with ScalafmtModule with DockerModule {
    override def ivyDeps = super.ivyDeps() ++ Versions.jvmDeps

    override def resources = T.sources {
      super.resources() :+ frontend.webpackBundle()
    }

    object docker extends DockerConfig {
      override def tags = List("giymo11/lara-backend")
    }

    override def mainClass = Some("science.wasabi.lara.backend.Main")
  }
}
