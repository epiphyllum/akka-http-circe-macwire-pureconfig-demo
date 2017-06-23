package com.yimei.template.routes.captcha

import com.yimei.template.http.ExtensionDirectives.{Result, failed, ok}

import scala.concurrent.Future

/**
  * Created by hary on 2017/6/23.
  */
trait FindCaptchaController {

  import com.yimei.template.ApplicationContext._

  // 获取图片验证码
  def findCaptcha(clientId: String): Future[Result[String]] = {
    // if (coreConfig.getBoolean("test")) {
    if (false) {
      redisClient.get[String](s"$clientId:kap").map {
        case None => failed[String](400, "没有找到验证码")
        case Some(code) => ok(code)
      }
    } else {
      Future.successful(ok("999999"))
    }
  }

}
