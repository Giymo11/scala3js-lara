package science.wasabi.lara

import utest._

import scala.collection._

object KrakenTest extends TestSuite {
    val tests = Tests {
        test("test1") {
            val privateKey = "8p1uGOVjbgWA7FunAmGO8lsSUXNsu3eow76sz84Q18fWxnyRzBHCd3pd5nE9qa99HAZtuZuj6F1huXg=="
            val nonce = "1616492376594"
            val encodedPayload = "nonce=1616492376594&ordertype=limit&pair=XBTUSD&price=37500&type=buy&volume=1.25"
            val uriPath = "/0/private/AddOrder"

            val expectedSignature = "4/dpxb3iT4tp/ZCVEwSnEsLxx0bqyhLpdfOpc6fn7OR8+UClSV5n9E6aSS8MPtnRfp32bAb0nmbRn6H8ndwLUQ=="
            assert("Hello World" == Kraken.getMessageSignature(uriPath, encodedPayload, privateKey, nonce))
        }
        test("test2") {
            import sttp.client3._

            val nonce = "1616492376594"

            var request = Seq("ordertype" -> "limit", "pair" -> "XBTUS", "price" -> "37500", "type" -> "buy", "volume" -> "1.25")
            val reqAndNonce = Seq("nonce" -> nonce) ++ request

            val path = "/0/private/AddOrder"

            var base = uri"https://api.kraken.com/"
            val payload = base.path(path).params(reqAndNonce.toSeq: _*)

            println(payload)
        }
    }
}