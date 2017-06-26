package com.yimei.template.routes.captcha

import akka.http.scaladsl.server.Directives._
import com.yimei.template.context.Context
import com.yimei.template.http.ExtensionDirectives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

/**
  * Created by hary on 2017/6/22.
  */
class CaptchaRoute(systemName: String) extends CheckCaptchaController
  with FindCaptchaController
  with GetCaptchaController {

  val log = Context.getLogger(this)

  def route = pathPrefix(systemName) {
    cookie(systemName) { cookie =>
      log.info(s"captcha: get cookie ${cookie.name} -> ${cookie.value}")
      pathPrefix("captcha") {
        // 获取验证码 /upstream/captcha/get, res:  jpg图片 + cookie
        path("get") {
          complete(handleGetCaptcha(cookie.value)) // 这里就已经是个HttpResponse了
        } ~
          // 校验验证码 /upstream/captcha/check/:code, res: { sucess: true, data: true}
          path("check" / Segment) { code =>
            onSuccess(handleCheckCaptcha(cookie.value, code)) {
              case true => complete(ok(true))
              case false => complete(failed[Boolean](400, "图形验证码错误"))
            }
          } ~
          path("find") {
            complete(findCaptcha(cookie.value))
          }
      }
    }
  }
}

object CaptchaRoute {
  def apply() =
    new CaptchaRoute("upstream").route ~
      new CaptchaRoute("capital").route
}