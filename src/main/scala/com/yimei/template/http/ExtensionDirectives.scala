package com.yimei.template.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, Directive1}
import com.yimei.template.ApplicationContext.appConfig
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.circe.Encoder
import io.circe.jawn.decode
import pdi.jwt.exceptions.JwtValidationException
import pdi.jwt.{Jwt, JwtAlgorithm}

import scala.concurrent.Future
import scala.util.Success
import scala.util.control.NonFatal
import scala.util.matching.Regex
import com.yimei.template.ApplicationContext._

/**
  * Created by hary on 2017/6/23.
  */
object ExtensionDirectives {

  // API接口定义
  case class Pager(pageSize: Option[Int], pageNum: Option[Int]);
  case class Pagination(totalCount: Long = 0, totalPage: Long = 0, pageSize: Int = 10, curPage: Int = 1)
  case class Error(code: Int, message: String)
  case class Result[T](data: Option[T] = None, success: Boolean, error: Option[Error] = None)
  case class PageItems[T](items: Seq[T] = List(), pagination: Pagination)
  case class JwtSession(id: String, username: String, phone: String, roleType: Int, systemId: Int)


  // fail + success, 将一个T 包装成Result[T], 将一个Seq[T] + Pagination包装成Result[M]
  def failed[T](code: Int, message: String) = Result[T](None, false, Some(Error(code, message)))
  def failed[T](error: Error) = Result[T](None, false, Some(error))
  def ok[T](data: T): Result[T] = Result(Some(data), success = true)
  def ok[T](items: Seq[T], meta: Pagination): Result[PageItems[T]] = Result(Some(PageItems(items, meta)), success = true)
  def fok[T](df: Future[T]) = df map (ok(_))

  // 指令, 将T, Future[T]包装为Result[T], Future[Result[T]] 并complete
  def result[T:Encoder](t: T) = complete(ok(t))
  def result[T:Encoder](tf: Future[T]) = complete(tf.map(ok(_)))

  // 指令 check
  def check(t: (Boolean, String)) = validate(t._1, t._2)

  // 为case class 生成validator
  import com.wix.accord.{Failure => VFailure, Success => VSuccess, Validator, validate => kvalidate}
  def genValidator[T: Validator](t: T) = {
    kvalidate(t) match {
      case VFailure(violations) => (false, violations.mkString(","))
      case VSuccess => (true, "")
    }
  }

  // 指令 - 获取分页参数  Symbol
  def page(defaultPageSize: Int = 10): Directive[Tuple1[Pager]] =
    parameters('pageSize.?, 'pageNum.?).tmap {
      t => Pager(
          t._1.map(_.toInt).orElse(Some(defaultPageSize)),
          t._2.map(_.toInt).orElse(Some(1)))
    }

  // 指令 - 获取JwtSession
  def jwt: Directive1[JwtSession] =
    headerValueByName("Authorization") map { auth =>
      try {
        val regex: Regex = """Bearer (\S+)""".r
        val regex(token) = auth
        Jwt.decode(token, appConfig.jwt.jwtSecret, Seq(JwtAlgorithm.HS256)).map { (claim: String) =>
          decode[JwtSession](claim) match {
            case Right(jwt) => jwt
            case _ => throw new JwtValidationException("token error")
          }
        } match {
          case Success(jwt) => jwt
          case _ => throw new JwtValidationException("token error")
        }
      } catch {
        case NonFatal(e) =>
          e.printStackTrace()
          throw new JwtValidationException("token error")
      }
    }
}

