package science.wasabi.lara.backend

import zhttp.http._
import zhttp.http.Response.HttpResponse
import zio.Chunk

object ZhttpHelper {
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

object Helper {
  def openWebPage(location: String): Unit = {
    import java.awt.Desktop
    import java.net._

    if(!Desktop.isDesktopSupported()) {
      println("Desktop not supported")
    } else {
      val uri = URL(location).toURI
      println(s"browsing to $uri")
      Desktop.getDesktop().browse(uri)
    }
  }
}
