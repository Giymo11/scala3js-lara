package science.wasabi.lara.backend

object StaticFiles {
  import os.{GlobSyntax, /}

  val indexHtml = os.read(os.resource / "index.html")
  // this is inserted by the build tool from the "frontend" module
  val bundleJs = os.read(os.resource / "out.js")
  val bundleJsMap = os.read(os.resource / "out.js.map")
}

object ZhttpHelper {
  import zio.*
  import zhttp.http.*
  import zhttp.http.Response.HttpResponse

  def httpDataFromString(s: String): HttpData[Any, Nothing] =
    HttpData.CompleteData(Chunk.fromArray(s.getBytes(HTTP_CHARSET).nn))

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

object OsHelper {
  def openWebPage(location: String): Unit = {
    import java.awt.Desktop
    import java.net.*

    if !Desktop.isDesktopSupported() then {
      println("Desktop not supported")
    } else {
      val desktop = Desktop.getDesktop()
      if (desktop == null) then println("desktop could not be created")
      else {
        val uri = URL(location).toURI
        println(s"browsing to $uri")
        desktop.browse(uri)
      }
    }
  }
}
