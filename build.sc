import $ivy.`com.lihaoyi::mill-contrib-docker:$MILL_VERSION`
import $ivy.`com.lihaoyi::mill-contrib-bloop:$MILL_VERSION`

import mill._
import mill.scalajslib._
import scalalib._
import scalafmt._

import contrib.docker.DockerModule

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
  }

  trait Common extends ScalaModule {
    override def scalaVersion = Versions.scala

    def ivyDeps = super.ivyDeps() ++ Versions.sharedDeps

    def sources = T.sources(
      millSourcePath / "src",
      millSourcePath / os.up / "shared" / "src"
    )
  }

  object shared extends Common

  object frontend extends Common with ScalaJSModule with ScalafmtModule {
    override def scalaJSVersion = Versions.scalajs

    def ivyDeps = super.ivyDeps() ++ Versions.jsDeps

    def mainClass = Some("science.wasabi.lara.frontend.Main")
  }

  object backend extends Common with ScalafmtModule with DockerModule {
    def ivyDeps = super.ivyDeps() ++ Versions.jvmDeps

    def resources = T.sources {
      super.resources() :+ PathRef(frontend.fastOpt().path / os.up)
    }

    object docker extends DockerConfig {
      def tags = List("giymo11/lara-backend")
    }

    def mainClass = Some("science.wasabi.lara.backend.Main")
  }
}
