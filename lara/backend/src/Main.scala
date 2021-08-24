package science.wasabi.lara.backend

import zio._
import zhttp.http._
import zhttp.service.Server

object Main extends App {
  println("Hello World")

  val port = 8090

  val serverImpl = Routes.helloWorld <> Routes.staticRoutes <> Routes.notFound

  val appLogic = for {
    serverFiber <- Server.start(port, serverImpl).forkDaemon
    _ <- ZIO(OsHelper.openWebPage(s"http://localhost:$port"))
    _ <- serverFiber.join // to block the main thread from finishing
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    appLogic.exitCode
}
