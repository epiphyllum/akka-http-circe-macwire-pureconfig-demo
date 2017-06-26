package com.yimei.template

import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives.logRequestResult
import akka.http.scaladsl.server.{Route, RouteResult}
import akka.http.scaladsl.server.directives.LogEntry
import akka.stream.scaladsl.Sink
import com.yimei.template.routes.asset.AssetRoute
import com.yimei.template.routes.upstream.UpstreamRoute

import scala.concurrent.Future
import akka.http.scaladsl.server.Directives._
import com.yimei.template.routes.ResourceRoute
import com.yimei.template.routes.mock.MockRoute

/**
  * Created by hary on 2017/6/22.
  */
object JfcApp extends App {

  import com.yimei.template.context.Context._
  val log = getLogger(this)

  // 准备路由
  val route = UpstreamRoute() ~
  AssetRoute() ~
  ResourceRoute() ~
  MockRoute()

  // 启动app
  startHttp(route)

  def startHttp(route: Route) = {

    import com.yimei.template.http.ExceptionConfig.myExceptionHandler
    import com.yimei.template.http.RejectionConfig.myRejectionHandler

    def extractLogEntry(req: HttpRequest): RouteResult => Option[LogEntry] = {
      case RouteResult.Complete(res) => Some(LogEntry(req.method.name + " " + req.uri + " => " + res.status, Logging.InfoLevel))
      case _ => None // no log entries for rejections
    }

    val wrapper: Route = logRequestResult(extractLogEntry _)(route)

    log.info("http is listening on {}", appConfig.server.port)
    val server = Http(system).bind("0.0.0.0", appConfig.server.port)

    import akka.http.scaladsl.server.Route._
    val handler: Future[ServerBinding] =
      server
        .to(Sink.foreach { connection => connection.handleWithAsyncHandler(asyncHandler(wrapper)) })
        .run()
    handler.failed.foreach { case ex: Exception ⇒ log.error(ex, "Failed to bind to :{}", appConfig.server.port) }
  }
}

