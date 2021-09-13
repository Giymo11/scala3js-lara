package science.wasabi.lara

import scala.scalajs.js

import sttp.client3.*

import typings.cryptoJs.{mod as cryptojs}

object Kraken {

  private def uriEncode(data: Seq[(String, String)]) = {
    val payload = uri"http://localhost/".withParams(data.toSeq*)
    payload.toJavaUri.getQuery()
  }

  def getMessageSignature(
      path: String,
      params: scala.collection.Seq[(String, String)],
      encodedSecret: String,
      nonce: String
  ) = {
    import cryptojs.enc.*

    val reqAndNonce = Seq("nonce" -> nonce) ++ params

    val postdata = uriEncode(reqAndNonce)
    val hashDigest = cryptojs.SHA256(nonce + postdata, ())

    val secret = Base64.parse(encodedSecret)

    val pathAndHash = Utf8.parse(path).concat(hashDigest)
    val hmacDigest = cryptojs.HmacSHA512(pathAndHash, secret)

    Base64.stringify(hmacDigest)
  }
}
