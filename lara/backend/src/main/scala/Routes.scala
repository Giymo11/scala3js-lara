package science.wasabi.lara.backend

import zio.*
import zhttp.http.*

import science.wasabi.lara.Endpoints

object Routes {
  import sttp.tapir.server.ziohttp.ZioHttpInterpreter

  private def helloWorldLogic(name: String) = ZIO.succeed(Right(s"Hello $name"))
  val helloWorld = ZioHttpInterpreter().toHttp(Endpoints.helloWorldEndpoint)(helloWorldLogic)

  val staticRoutes = Http.collect[Request] {
    case Method.GET -> Root / "hello"      => Response.text("Hello World!")
    case Method.GET -> Root                => ZhttpHelper.respondWithHtml(StaticFiles.indexHtml)
    case Method.GET -> Root / "index.html" => ZhttpHelper.permanentRedirectTo("/")
    case Method.GET -> Root / "out.js"  => ZhttpHelper.respondWithJs(StaticFiles.bundleJs)
    // case Method.GET -> Root / "out.js.map"  => ZhttpHelper.respondWithJs(StaticFiles.bundleJsMap)
  }

  val notFound = Http.collect[Request] {
    case _ => Response.text("404 Not Found!")
  }
}
