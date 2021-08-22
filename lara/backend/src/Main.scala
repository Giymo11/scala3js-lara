package science.wasabi.lara.backend

import zhttp.http._
import zhttp.service.Server
import zio._
import zhttp.http.Response.HttpResponse


object Main extends App {
  println("Hello World")

  object StaticFiles {
    import os.{GlobSyntax, /}

    val indexHtml = os.read(os.resource/"index.html")
    val bundleJs = ""
  }

  object StaticResponses {
    def httpDataFromString(s: String): HttpData[Any, Nothing] = HttpData.CompleteData(Chunk.fromArray(s.getBytes(HTTP_CHARSET)))
    val contentTypeHtml = Header.custom("content-type", "text/html")

    val indexHtml = Response.http(
      content = httpDataFromString(StaticFiles.indexHtml),
      headers = List(contentTypeHtml)
    )

    val redirectToRoot = Response.http(
      status = Status.PERMANENT_REDIRECT, 
      headers = List(Header.custom("location", "/")), 
      content = HttpData.empty)
  }

  val app = Http.collect[Request] {
    case Method.GET -> Root / "hello" => Response.text("Hello World!")
    case Method.GET -> Root => StaticResponses.indexHtml
    case Method.GET -> Root / "index.html" => StaticResponses.redirectToRoot
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Server.start(8090, app).exitCode
}

