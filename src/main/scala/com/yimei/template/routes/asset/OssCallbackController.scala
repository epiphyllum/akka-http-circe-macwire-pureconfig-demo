package com.yimei.template.routes.asset

import com.yimei.template.WithLog

import scala.concurrent.Future

/**
  * Created by hary on 2017/6/23.
  */
trait OssCallbackController extends WithLog {

  import com.yimei.template.ApplicationContext._
  import com.yimei.template.http.ExtensionDirectives._

  case class OssCallbackResponse(success: Boolean, fileName: String, mimeType: String, realname: String)

  def handleOssCallback(filename: String, mimeType: String, realname: String): Future[Result[OssCallbackResponse]] = {
    log.info("get {} {} {}", filename, mimeType, realname)
    assetService.insertUploadRecord(filename, mimeType, realname).map {
      case true => ok(OssCallbackResponse(true, filename, mimeType, realname))
      case false => ok(OssCallbackResponse(false, filename, mimeType, realname))
    }
  }
}
