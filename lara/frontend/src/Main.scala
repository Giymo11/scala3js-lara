package science.wasabi.lara.frontend

import org.scalajs.dom
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

import com.raquo.laminar.api.L.{*, given}

import science.wasabi.lara.*

object Main extends App {
  println("Hello world!")

  Helper.testHelloWorld()

  @js.native
  @JSImport("uuid", JSImport.Default)
  object Uuid extends js.Object {
    @js.native
    object v4 extends js.Object {
      def apply(): String = js.native
    }
  }

  println(Uuid.v4())
}
