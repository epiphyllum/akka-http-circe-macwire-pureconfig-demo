package com.yimei.template

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directive0, RequestContext}

import scala.concurrent.Future
import scala.util.Success
import akka.http.scaladsl.server.directives.BasicDirectives._
import akka.http.scaladsl.server.directives.RouteDirectives._
import akka.http.scaladsl.server.directives.FutureDirectives._
import com.yimei.template.http.ExtensionDirectives._
import io.circe.syntax._
import io.circe.generic.auto._

/**
  * Created by hary on 2017/6/22.
  */
object BusinessDirectives {

  //验证公司状态
  def companyAuth(session: JwtSession): Directive0 = {
    // def mycheck(ctx: RequestContext): Future[Boolean] = companyIsVerifyed(session)
    def mycheck(ctx: RequestContext): Future[Boolean] = Future.successful(true)

    extractExecutionContext.flatMap { implicit ec ⇒
      extract(mycheck).flatMap[Unit] { fa ⇒
        onComplete(fa).flatMap {
          case Success(true) ⇒ pass
          case _ ⇒ complete(HttpResponse(StatusCodes.BadRequest, entity = failed[String](4000, "公司状态异常").asJson.toString))
        }
      }
    }
  }
}
