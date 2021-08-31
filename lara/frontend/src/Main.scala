package science.wasabi.lara.frontend

import org.scalajs.dom
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

import com.raquo.laminar.api.L.{given, *}

import science.wasabi.lara.*

import typings.uuid.{mod as Uuid}
import com.raquo.airstream.state.Var

object Main extends App {
  println("Hello world!")

  Helper.testHelloWorld()

  println("" + Uuid.v4(()))

  val containerNode = dom.document.querySelector("#laminarContainer")

  val apiKey: Var[String] = Var(initial = "")
  val apiSecret = Var(initial = "")


  def inputComponent(desc: String, variable: Var[String]) = div(
    label(s"$desc: "),
    input(
      placeholder := s"Enter your $desc here",
      inContext { thisNode => onInput.map(_ => thisNode.ref.value) --> variable }
    )
  )

  val app = div(
    inputComponent("Kraken Public Key", apiKey),
    inputComponent("Kraken Secret Key", apiSecret)
  )



  render(containerNode, app)
}
