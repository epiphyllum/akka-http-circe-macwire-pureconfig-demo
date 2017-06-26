package com.yimei.template.routes.upstream

import com.yimei.template.context.Context._
import com.yimei.template.http.ExtensionDirectives.{Result, ok}
import nl.grons.metrics.scala.DefaultInstrumented

import scala.concurrent.Future

/**
  * Created by hary on 2017/6/23.
  */
trait GetHelloController extends WithLog with  DefaultInstrumented {
  // 服务
  case class GetRequest(message: String)
  case class GetResponse(message: String)

  import com.yimei.template.context.Context._
  def handleGet(req: GetRequest): Future[Result[GetResponse]] = {
    Future {
      ok(GetResponse(req.message))
    }
  }
}
