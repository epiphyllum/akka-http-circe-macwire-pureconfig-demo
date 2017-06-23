package com.yimei.template.http

import akka.http.scaladsl.model.StatusCodes.{BadRequest, MethodNotAllowed, NotFound, Unauthorized}
import akka.http.scaladsl.model.{HttpResponse, StatusCode, Uri}
import akka.http.scaladsl.server.Directives.{complete, extractUri}
import akka.http.scaladsl.server._
import io.circe.generic.auto._
import io.circe.syntax._

/**
  * Created by hary on 2017/6/23.
  */
object RejectionConfig {

  import ExtensionDirectives._
  import com.yimei.template.ApplicationContext._

  val log = getLogger(this)

  private[this] def handle(errCode: Int, status: StatusCode, msg: String = "") = {
    extractUri { uri =>
      log.warning("reject request {}, erroCode: {}, status: {}, msg: {}", uri, errCode, status, msg)
      complete(HttpResponse(
        status,
        entity = failed[String](errCode, msg).asJson.toString
      ))
    }
  }

  // 指定的rejection处理
  private val rejectCases: PartialFunction[Rejection, Route] = {
    case MissingCookieRejection(cookieName)       => handle(5001, BadRequest, s"cookie $cookieName needed")
    case MissingQueryParamRejection(name)         => handle(5002, BadRequest, s"missing request parameter $name")
    case AuthorizationFailedRejection             => handle(5003, Unauthorized, s"authorization failed")
    case ValidationRejection(msg, _)              => handle(5004, BadRequest, s"validation: $msg")
    case MissingHeaderRejection(header)           => handle(5005, BadRequest, s"missing header $header")
    case MalformedRequestContentRejection(msg, _) => handle(5006, BadRequest, msg)
  }

  implicit val myRejectionHandler =
    RejectionHandler.newBuilder()
      .handle(rejectCases)
      .handleAll[MethodRejection] { methodRejections =>
      extractUri { uri =>
        log.info("asdadsfaf")
        val names = methodRejections.map(_.supported.name).mkString(",")
        handle(5555, MethodNotAllowed, s"only support method: $names")
      }
    }.handleNotFound {
      handle(6666, NotFound, "uri not found")
    }.result()
}
