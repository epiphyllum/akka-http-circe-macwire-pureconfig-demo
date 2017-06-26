package com.yimei.template.routes.mock

import com.wix.accord.dsl._
import com.yimei.template.dict.CirceEnumCodec
import com.yimei.template.http.ExtensionDirectives._
import io.circe._
import nl.grons.metrics.scala.DefaultInstrumented

import scala.concurrent.Future
import scala.util.Try

/**
  * Created by hary on 2017/6/23.
  */
trait MockController extends DefaultInstrumented {

  case class ControllerRequest(message: String) {

    // 这里是请求校验
    def validate(): (Boolean, String) = genValidator(this) {
      validator[ControllerRequest] { mr =>
        mr.message must startWith("error")
      }
    }

    // 这里是业务检查, 函数签名只有一个要求， boolean + string 的tuple
    def validate2(): (Boolean, String) = {
      // 查询数据库， 检验用户情况 etc...
      (false, "business validation failed")
    }

    // 这是业务控制检查
    def authorize = {
      Future.successful(false)
    }
  }

  case class ControllerResponse(message: String)
  def handlePost(req: ControllerRequest) = {
    fok(Future.successful(ControllerResponse(req.message + " echo back")))
  }


  import com.yimei.template.dict.CompanyType._
  case class HasEnum(companyType: CompanyType, name: String)
  def handleEnum() = {
    ok(HasEnum(SOE_CAP_ABS, "hary"))
  }

}
