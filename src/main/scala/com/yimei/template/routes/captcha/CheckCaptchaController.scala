package com.yimei.template.routes.captcha

import scala.concurrent.Future

/**
  * Created by hary on 2017/6/23.
  */
trait CheckCaptchaController {

  import com.yimei.template.ApplicationContext._

  // 校验验证码
  def handleCheckCaptcha(clientId: String, code: String): Future[Boolean] = {
    redisClient.get[String](s"$clientId:kap").map { (store: Option[String]) =>
      // log.debug(s"got from reids[$store], code[$code], clientId[$clientId]")
      store.getOrElse("") == code
    }
  }
}
