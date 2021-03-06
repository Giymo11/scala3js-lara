package science.wasabi.lara.backend

import zio.*
import zhttp.http.*
import zhttp.service.Server

import science.wasabi.lara.*
import science.wasabi.lara.backend.*

object Main extends App {
  println("Hello World")

  val serverImpl = Routes.helloWorld <> Routes.staticRoutes <> Routes.notFound

  val appLogic = for
    serverFiber <- Server.start(Config.port, serverImpl).forkDaemon
    // _ <- ZIO(OsHelper.openWebPage(Config.apiLocation))
    _ <- serverFiber.join // to block the main thread from finishing
  yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    appLogic.exitCode
}
