package com.yimei.template.routes.upstream

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.template.ApplicationContext
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._


/**
  * Created by hary on 2017/6/22.
  */
class UpstreamRoute extends GetHelloController
  with PostHelloController {

  val log = ApplicationContext.getLogger(this)

  def route: Route = pathPrefix("upstream") {
    // get hello 请求
    (path("hello" / Segment) & get) { req =>
      complete(handleGet(GetRequest(req)))
    } ~
      // post hello
      (path("hello") & post & entity(as[PostRequest])) { post =>
        complete(handlePost(post))
      }
  }
}

object UpstreamRoute {
  def apply() = (new UpstreamRoute()).route
}

