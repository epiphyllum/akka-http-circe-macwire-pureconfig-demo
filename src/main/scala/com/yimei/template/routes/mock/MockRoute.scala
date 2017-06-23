package com.yimei.template.routes.mock

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import com.yimei.template.ApplicationContext
import com.yimei.template.http.ExtensionDirectives._

import scala.concurrent.Future

/**
  * Created by hary on 2017/5/15.
  */
class MockRoute {

  val log = ApplicationContext.getLogger(this)

  case class MockRequest(message: String)
  case class MockResponse[T](message: String, t: T)

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
        result(Future{ MockResponse("ok", 1)})
      }
  }
}

object MockRoute {
  def apply() = (new MockRoute).route
}

