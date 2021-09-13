package science.wasabi.lara.frontend

import org.scalajs.dom
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

import com.raquo.laminar.api.L.{given, *}

import science.wasabi.lara.*

import typings.uuid.{mod as Uuid}
import com.raquo.airstream.state.Var
import scala.scalajs.js.URIUtils

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


  val userAgent = "Lara/0.1"
  val contentType = "application/x-www-form-urlencoded"

  import sttp.client3.*

  var base = uri"https://api.kraken.com/"
  val nonce = "1616492376594"
  val path = "/0/private/AddOrder"
  val encodedPrivateKey = "kQH5HW/8p1uGOVjbgWA7FunAmGO8lsSUXNsu3eow76sz84Q18fWxnyRzBHCd3pd5nE9qa99HAZtuZuj6F1huXg=="
  var request = Seq(
    "ordertype" -> "limit",
    "pair" -> "XBTUSD",
    "price" -> "37500",
    "type" -> "buy",
    "volume" -> "1.25"
  )

  val signature = Kraken.getMessageSignature(path, request, encodedPrivateKey, nonce)

  println(signature)
}
