package com.yimei.template.routes.upstream

import com.yimei.template.WithLog
import com.yimei.template.http.ExtensionDirectives.{Result, ok}

import scala.concurrent.Future

/**
  * Created by hary on 2017/6/23.
  */
trait GetHelloController extends WithLog {
  // 服务
  case class GetRequest(message: String)
  case class GetResponse(message: String)

  import com.yimei.template.ApplicationContext._
  def handleGet(req: GetRequest): Future[Result[GetResponse]] = {
    Future {
      ok(GetResponse(req.message))
    }
  }
}
