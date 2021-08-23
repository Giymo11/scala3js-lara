package science.wasabi.lara.backend

import zhttp.http._
import zhttp.service.Server
import zio._

import science.wasabi.lara.backend.Helper

object Main extends App {
  println("Hello World")

  val port = 8090

  object StaticFiles {
    import os.{GlobSyntax, /}

    val indexHtml = os.read(os.resource/"index.html")
    // this is inserted by the build tool from the "frontend" module
    val bundleJs = os.read(os.resource/"out.js")
  }

  val serverLogic = Http.collect[Request] {
    case Method.GET -> Root / "hello" => Response.text("Hello World!")
    case Method.GET -> Root => ZhttpHelper.respondWithHtml(StaticFiles.indexHtml)
    case Method.GET -> Root / "index.html" => ZhttpHelper.permanentRedirectTo("/")
    case Method.GET -> Root / "bundle.js" => ZhttpHelper.respondWithJs(StaticFiles.bundleJs)
  }

  val appLogic = for {
    serverFiber <- Server.start(port, serverLogic).forkDaemon
    _ <- ZIO(Helper.openWebPage(s"http://localhost:$port"))
    _ <- serverFiber.join // to block the main thread from finishing
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    appLogic.exitCode
}

