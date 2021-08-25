package science.wasabi.lara.frontend

import org.scalajs.dom

import sttp.client3.*
import sttp.tapir.client.sttp.SttpClientInterpreter
import sttp.model.Uri
import sttp.capabilities.WebSockets

import com.raquo.laminar.api.L.{*, given}
import com.raquo.airstream.eventbus.EventBus
import com.raquo.airstream.ownership.OneTimeOwner

import science.wasabi.lara.*

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.util.Failure

object Main extends App {
  println("Hello world!")

  implicit val sttpBackend: SttpBackend[concurrent.Future, WebSockets] = FetchBackend()
  implicit val owner: Owner = OneTimeOwner(() => println("accessed after kill"))

  def createHelloWorldClient(location: String): String => Future[String] = {
    Uri.parse(location) match {
      case Right(baseUri) => 
        println(s"sending request to $baseUri")
        // quick client has no websocket
        SttpClientInterpreter().toQuickClientThrowErrors(
          Endpoints.helloWorldEndpoint,
          Some(baseUri)
        )
      case Left(error) => 
        (_: String) => 
          println(s"client could not be created, error: $error")
          Future.failed(Exception(s"found: $error"))
    }
  }

  val client: String => Future[String] = createHelloWorldClient(Config.apiLocation)

  val eventBus = new EventBus[String]
  eventBus.events.foreach(x => println(s"Eventbus: $x"))

  val request: Future[String] = client.apply("Giymo11")
  eventBus.writer.addSource(EventStream.fromFuture(request))

  eventBus.emit("hello")

  request.foreach(println)
}
