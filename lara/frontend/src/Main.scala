package science.wasabi.lara.frontend

import org.scalajs.dom
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

import com.raquo.laminar.api.L.{given, *}

import science.wasabi.lara.*

import typings.uuid.{mod as Uuid}

object Main extends App {
  println("Hello world!")

  Helper.testHelloWorld()

  println("" + Uuid.v4(()))

  val containerNode = dom.document.querySelector("#laminarContainer")

  val nameVar = Var(initial = "world")

  val rootElement = div(
    label("Your name: "),
    input(
      onMountFocus,
      placeholder := "Enter your name here",
      inContext { thisNode => onInput.map(_ => thisNode.ref.value) --> nameVar }
    ),
    span(
      "Hello, ",
      child.text <-- nameVar.signal.map(_.toUpperCase.nn)
    )
  )

  render(containerNode, rootElement)
}
