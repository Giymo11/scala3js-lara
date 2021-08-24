package science.wasabi.lara.frontend

import org.scalajs.dom

import sttp.client3._
import sttp.tapir.client.sttp.SttpClientInterpreter

import science.wasabi.lara._

import concurrent.ExecutionContext.Implicits.global

object Main {
  
  def main(args: Array[String]): Unit = {
    println("Hello world!")

    val sttpBackend = FetchBackend()

    val client = SttpClientInterpreter().toQuickClient(Endpoints.helloWorldEndpoint, Some(uri"http://localhost:8090"))

    client("Giymo11").onComplete(res => println(res))

    sttpBackend.close()
  }
}