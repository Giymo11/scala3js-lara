package science.wasabi.lara.frontend

import org.scalajs.dom
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

import com.raquo.laminar.api.L.{*, given}

import science.wasabi.lara.*

import typings.uuid.{mod as Uuid}

object Main extends App {
  println("Hello world!")

  Helper.testHelloWorld()

  println(Uuid.v4(()))
}
