package science.wasabi.lara.backend

import zhttp.http._
import zhttp.service.Server
import zio._

import science.wasabi.lara.backend.Helper

object Main extends App {
  println("Hello World")

  object StaticFiles {
    import os.{GlobSyntax, /}

    val indexHtml = os.read(os.resource/"index.html")
    // this is inserted by the build tool from the "frontend" module
    val bundleJs = os.read(os.resource/"out.js")
  }

  val app = Http.collect[Request] {
    case Method.GET -> Root / "hello" => Response.text("Hello World!")
    case Method.GET -> Root => Helper.respondWithHtml(StaticFiles.indexHtml)
    case Method.GET -> Root / "index.html" => Helper.permanentRedirectTo("/")
    case Method.GET -> Root / "bundle.js" => Helper.respondWithJs(StaticFiles.bundleJs)
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Server.start(8090, app).exitCode
}

