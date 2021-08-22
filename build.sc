import mill._, scalalib._
import scalafmt._
import mill.scalajslib._

import $ivy.`com.lihaoyi::mill-contrib-docker:$MILL_VERSION`
import contrib.docker.DockerModule

object lara extends Module {
  object Versions {
    val scala = "3.0.1"
    val scalajs = "1.7.0"
    
    val zio = "1.0.0.0-RC17"
    val osLib = "0.7.8"

    val jvmDeps = Agg(
      ivy"io.d11::zhttp:$zio",
      ivy"com.lihaoyi::os-lib:$osLib"
    )

    val jsDeps = Agg(
      // has to be this way because sjsdom is not published for scala 3
      ivy"org.scala-js:scalajs-dom_sjs1_2.13:1.1.0" 
    )
  }

  object frontend extends ScalaJSModule with ScalafmtModule {
    override def scalaVersion = Versions.scala
    override def scalaJSVersion = Versions.scalajs

    def ivyDeps = super.ivyDeps() ++ Versions.jsDeps

    def mainClass = Some("science.wasabi.lara.frontend.Main")
  }
  
  object backend extends ScalaModule with ScalafmtModule with DockerModule {
    override def scalaVersion = Versions.scala
    
    def ivyDeps = super.ivyDeps() ++ Versions.jvmDeps

    def resources = T.sources {
      import ammonite.ops.up
      super.resources() :+ PathRef(frontend.fastOpt().path / up)
    }

    object docker extends DockerConfig {
      def tags = List("giymo11/lara-backend")
    }

    def mainClass = Some("science.wasabi.lara.backend.Main")
  }
}

