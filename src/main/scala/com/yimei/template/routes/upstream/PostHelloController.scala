package com.yimei.template.routes.upstream

import com.yimei.template.WithLog
import com.yimei.template.http.ExtensionDirectives.{Result, ok}

import scala.concurrent.Future

/**
  * Created by hary on 2017/6/23.
  */
trait PostHelloController extends WithLog {

  import com.yimei.template.ApplicationContext._
  case class PostRequest(message: String)
  case class PostResponse(message: String)
  def handlePost(req: PostRequest): Future[Result[PostResponse]] = {
    Future {
      ok(PostResponse(req.message))
    }
  }
}
