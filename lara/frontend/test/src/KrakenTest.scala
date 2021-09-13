package science.wasabi.lara

import utest.*

import scala.collection.*

import scala.scalajs.js
import js.annotation.*
import js.typedarray.*

@js.native
@JSGlobal
class TextEncoder(utfLabel: js.UndefOr[String]) extends js.Object {
  def encode(buffer: String): Uint8Array = js.native
}

object KrakenTest extends TestSuite {
  val tests = Tests {
    test("test1") {

      val userAgent = "Lara/0.1"
      val contentType = "application/x-www-form-urlencoded"

      val path = "/0/private/AddOrder"
      var request = Seq(
        "ordertype" -> "limit",
        "pair" -> "XBTUSD",
        "price" -> "37500",
        "type" -> "buy",
        "volume" -> "1.25"
      )
      val encodedPrivateKey = "kQH5HW/8p1uGOVjbgWA7FunAmGO8lsSUXNsu3eow76sz84Q18fWxnyRzBHCd3pd5nE9qa99HAZtuZuj6F1huXg=="
      val nonce = "1616492376594"

      val expectedResult = "4/dpxb3iT4tp/ZCVEwSnEsLxx0bqyhLpdfOpc6fn7OR8+UClSV5n9E6aSS8MPtnRfp32bAb0nmbRn6H8ndwLUQ=="
      val result = Kraken.getMessageSignature(path, request, encodedPrivateKey, nonce)
      assert(result == expectedResult)
    }
  }
}
