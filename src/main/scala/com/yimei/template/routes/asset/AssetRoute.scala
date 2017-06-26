package com.yimei.template.routes.asset

import akka.http.scaladsl.server.Directives._
import com.yimei.template.http.ExtensionDirectives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

/**
  * Created by hary on 2017/6/22.
  */
class AssetRoute() extends OssCallbackController with PolicyController {

  import com.yimei.template.context.Context._
  val log = getLogger(this)

  def route = pathPrefix("asset") {
    // 获取上传策略
    (path("policy") & get & jwt) { jwtSession: JwtSession =>
      complete(handleUploadPolicy(jwtSession.id))
    } ~
      // 阿里云上传回调
      path("callback") {
        formFields("filename", "mimeType", "realname") { (filename, mimeType, realname) =>
          complete(handleOssCallback(filename, mimeType, realname))
        }
      }
  }
}

object AssetRoute {
  def apply() = (new AssetRoute).route
}
