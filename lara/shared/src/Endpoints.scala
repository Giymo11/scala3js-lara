package science.wasabi.lara

import sttp.tapir.*

object Endpoints {
  val helloWorldEndpoint = endpoint
    .in("greet")
    .in(query[String]("name").description("The name to greet"))
    .errorOut(stringBody)
    .out(stringBody)
}

