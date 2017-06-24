package com.yimei.template.routes.mock

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.wix.accord.dsl._
import com.yimei.template.ApplicationContext
import com.yimei.template.http.ExtensionDirectives._
import com.yimei.template.http.RejectionConfig.BusinessRejection
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.Future

/**
  * Created by hary on 2017/5/15.
  */
class MockRoute extends MockController {

  import ApplicationContext._

  val log = getLogger(this)

  case class MockRequest(message: String) {
    def validate() = genValidator(this) {
      validator[MockRequest] { mr =>
        mr.message must startWith("hello")
      }
    }
  }

  case class MockResponse[T](message: String, t: T) // , violations: List[Violation] = List())

  import ApplicationContext._

  def route: Route = pathPrefix("mock") {
    path("sms") {
      formFields('msgType, 'phone, 'signature, 'smsUser, 'templateId, 'vars) { (msgType, phone, signature, smsUser, templateId, vars) =>
        log.info(s"smsMock got: msgType[${msgType}], phone[${phone}], signature[${signature}], smsUser[${smsUser}], templateId[${templateId}], vars[${vars}]")
        complete(""""result":true""")
      }
    } ~
      (path("pageTest") & page(10) & parameters("a", "b")) { (p: Pager, a, b) =>
        println(s"a = $a, b = $b")
        result(p)
      } ~
      (path("circeTest") & post & entity(as[MockRequest])) { req =>
        check(req.validate()) {
          result(Future {
            MockResponse("ok", "100")
          })
        }
      } ~
      (path("controller") & post & entity(as[ControllerRequest])) { req =>
        (check(req.validate()) & check(req.businessValidate())) {
          complete(handlePost(req))
        }
      } ~
      (path("rejection")) {
        reject(BusinessRejection(1000, "this is BusinessReject Message"))
      }
  }
}

object MockRoute {
  def apply() = (new MockRoute).route
}

