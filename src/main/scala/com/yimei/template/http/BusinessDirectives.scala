package com.yimei.template.http

import akka.http.scaladsl.server.directives.BasicDirectives._
import akka.http.scaladsl.server.directives.FutureDirectives._
import akka.http.scaladsl.server.directives.RouteDirectives._
import akka.http.scaladsl.server.{Directive0, RequestContext}
import com.yimei.template.http.ExtensionDirectives._
import com.yimei.template.http.RejectionConfig.BusinessRejection

import scala.concurrent.Future
import scala.util.Success

/**
  * Created by hary on 2017/6/22.
  */
object BusinessDirectives {

  /**
    * 依据业务检查T => Future[Boolean]结果产生rejection:
    *
    * @param check
    * @param t
    * @param code
    * @param msg
    * @tparam T
    * @return
    */
  def authGen[T](t: T, code: Int, msg: String)(check: T => Future[Boolean]): Directive0 = {
    def mycheck(ctx: RequestContext): Future[Boolean] = check(t)

    extractExecutionContext.flatMap { implicit ec ⇒
      extract(mycheck).flatMap[Unit] { fa ⇒
        onComplete(fa).flatMap {
          case Success(true) ⇒ pass
          case _ ⇒ reject(BusinessRejection(code, msg))
        }
      }
    }
  }

  // 验证公司状态
  def companyAuth(session: JwtSession): Directive0 =
    authGen(session, 9001, "公司检查失败") { session =>
      Future.successful(false)
    }

}
