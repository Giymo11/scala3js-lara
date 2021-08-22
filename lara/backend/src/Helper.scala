package science.wasabi.lara.backend

import zhttp.http._
import zhttp.http.Response.HttpResponse
import zio.Chunk

object Helper {

  def httpDataFromString(s: String): HttpData[Any, Nothing] =
    HttpData.CompleteData(Chunk.fromArray(s.getBytes(HTTP_CHARSET)))

  val contentTypeHtml = Header.custom("content-type", "text/html")
  def respondWithHtml(html: String) = Response.http(
    content = httpDataFromString(html),
    headers = List(contentTypeHtml)
  )

  val contentTypeJs = Header.custom("content-type", "text/javascript")
  def respondWithJs(js: String) = Response.http(
    content = httpDataFromString(js),
    headers = List(contentTypeJs)
  )

  def permanentRedirectTo(location: String) = Response.http(
    status = Status.PERMANENT_REDIRECT,
    headers = List(Header.custom("location", location)),
    content = HttpData.empty
  )
}
