package com.yimei.template.http

import akka.http.scaladsl.model.{HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, extractUri}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.yimei.template.http.ExtensionDirectives._
import io.circe.generic.auto._
import io.circe.syntax._

import scala.util.control.NonFatal

/**
  * Created by hary on 2017/6/23.
  */
object ExceptionConfig {

  import com.yimei.template.ApplicationContext._

  val log = getLogger(this)

  // 异常
  case class DatabaseException(message: String) extends Exception(message)
  case class ParameterException(message: String) extends Exception(message)
  case class BusinessException(message: String) extends Exception(message)
  case class UnauthorizedException(message: String) extends Exception(message)
  case class NotFoundException(message: String) extends Exception(message)

  private[this] def handle(e: Throwable, errorCode: Int, status: StatusCode, msg: String = null): Route = {
    e.printStackTrace()
    // fail message
    val fmsg = if (msg == null) {
      if (e.getMessage == null)
        ""
      else
        e.getMessage
    } else {
      msg
    }

    extractUri { uri =>
      log.warning("request[{}] exception, errorCode[{}], status[{}], msg[{}]",
        uri, errorCode,status, fmsg)
      complete(HttpResponse(
        status,
        entity = failed[String](errorCode, fmsg).asJson.toString()
      ))
    }
  }


  // 异常处理器
  implicit val myExceptionHandler =
    ExceptionHandler {
      case e: DatabaseException => handle(e, 9001, StatusCodes.InternalServerError)
      case e: BusinessException => handle(e, 9002, StatusCodes.BadRequest)
      case NonFatal(e)          => handle(e, 9999, StatusCodes.InternalServerError)
    }
}
