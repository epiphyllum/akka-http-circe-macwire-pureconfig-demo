package com.yimei.template.services

import java.util.function.Consumer
import java.util.{Comparator, HashMap => JHashMap, TreeMap => JTreeMap}

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.util.ByteString
import com.yimei.template.context.Context._
import io.circe.syntax._
import org.apache.commons.codec.digest.DigestUtils

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by hary on 2017/6/23.
  */
class NotificationService {

  val log = getLogger(this)
  val smsConfig = appConfig.sendcloud.sms

  /**
    *
    * @param phone      发送给哪个手机
    * @param templateId 模板id
    * @param vars       模板变量
    * @return
    */
  def sendSms(phone: String, templateId: String, vars: Map[String, String]): Future[Boolean] = {

    log.info(s"${phone}------------------${vars}")
    // 填充参数
    val params = new JHashMap[String, String]();
    params.put("smsUser", smsConfig.smsUser);
    params.put("templateId", templateId);
    params.put("msgType", "0");
    params.put("phone", phone);
    params.put("vars", vars.asJson.toString())

    // 对参数进行排序
    val sortedMap = new JTreeMap[String, String](new Comparator[String]() {
      override def compare(o1: String, o2: String): Int = {
        return o1.compareToIgnoreCase(o2);
      }
    })
    sortedMap.putAll(params);

    // 计算签名
    val sb = new StringBuilder();
    sb.append(smsConfig.smsKey).append("&");
    val it = sortedMap.keySet().iterator()
    while (it.hasNext) {
      val key = it.next()
      sb.append(String.format("%s=%s&", key, sortedMap.get(key)));
    }
    sb.append(smsConfig.smsKey);
    val signature = DigestUtils.md5Hex(sb.toString());

    // 将签名放入map
    sortedMap.put("signature", signature)

    val postMap = collection.mutable.Map[String, String]()

    // 组post body
    sortedMap.keySet().forEach(
      new Consumer[String] {
        override def accept(t: String): Unit = {
          postMap += (t -> sortedMap.get(t))
        }
      }
    )
    val form = FormData(postMap.toMap)

    log.info("发送短信 url[{}], body[{}]", smsConfig.smsUrl, form.toEntity.toString)

    // 发送
    for {
      response <- Http().singleRequest(
        HttpRequest(
          uri = smsConfig.smsUrl,
          method = HttpMethods.POST,
          entity = form.toEntity
        )
      )
      entity <- response.entity.toStrict(30.seconds)
      entityBS <- entity.dataBytes.runFold(ByteString.empty) {
        case (acc, b) => acc ++ b
      }
    } yield {
      val pattern = """.*"result":(true|false).*""".r
      val entityStr = entityBS.utf8String
      println(s"got entityStr: $entityStr")
      val pattern(ok) = entityStr

      println("got result: " + ok)
      if (ok == "true") true else false
    }
  }
}



//    /**
//      *
//      * @param to
//      * @param from
//      * @param fromName
//      * @param subject
//      * @param html
//      * @return
//      */
//    def sendMail(to: String, from: String, fromName: String, subject: String, html: String) = {
//
//      val form: FormData = FormData(
//        "apiUser" -> mailUser,
//        "apiKey" -> mailKey,
//        "to" -> to,
//        "from" -> from,
//        "fromName" -> fromName,
//        "subject" -> subject,
//        "html" -> html
//      )
//
//      for {
//        response <- Http().singleRequest(
//          HttpRequest(
//            uri = mailUrl,
//            method = HttpMethods.POST,
//            entity = form.toEntity
//          )
//        )
//        entity <- response.entity.toStrict(30.seconds)
//        entityBS <- entity.dataBytes.runFold(ByteString.empty) {
//          case (acc, b) => acc ++ b
//        }
//      } yield {
//
//        if (response.status == StatusCodes.OK) {}
//
//        val pattern = """.*"result":(true|false).*""".r
//        val entityStr = entityBS.utf8String
//        println(s"got entityStr: $entityStr")
//        val pattern(ok) = entityStr
//        println("got result: " + ok)
//        if (ok == "true") true else false
//      }
//    }
//
//    /**
//      * @param to    发送邮件给谁
//      * @param from
//      * @param fromName
//      * @param subject  邮件主题
//      * @param html  邮件体内容
//      * @param filename 附件文件名称
//      * @param input 输入数据流
//      */
//    def sendMailWithAttachment(to: String, from: String, fromName: String, subject: String, html: String, filename: String, input: InputStream) {
//
//      val mff = HttpEntity(ContentTypes.`application/octet-stream`, StreamConverters.fromInputStream(() => input)).toStrict(30.seconds).map { entity =>
//        Multipart.FormData {
//          Map(
//            "apiUser" -> HttpEntity(mailUser),
//            "apiKey" -> HttpEntity(mailKey),
//            "to" -> HttpEntity(to),
//            "from" -> HttpEntity(from),
//            "fromName" -> HttpEntity(fromName),
//            "subject" -> HttpEntity(subject),
//            "html" -> HttpEntity(html),
//            "file" -> entity
//          )
//        }
//      }
//
//      for {
//        f <- mff
//        response <- Http().singleRequest(
//          HttpRequest(
//            uri = mailUrl,
//            method = HttpMethods.POST,
//            entity = f.toEntity()
//          )
//        )
//        entity <- response.entity.toStrict(30.seconds)
//        entityBS <- entity.dataBytes.runFold(ByteString.empty) {
//          case (acc, b) => acc ++ b
//        }
//      } yield {
//        if (response.status == StatusCodes.OK) {}
//        val pattern = """.*"result":(true|false).*""".r
//        val entityStr = entityBS.utf8String
//        println(s"got entityStr: $entityStr")
//        val pattern(ok) = entityStr
//        println("got result: " + ok)
//        if (ok == "true") true else false
//      }
//
//    }
//
//
//    def sendMailTemplate(recipents: List[String], from: String, fromName: String, subject: String, html: String, template: String) = {
//
//
//      // val a  = A("to1@ifaxin.com", "user1", "1000")
//
//      val xsmtpapi = "listofemail"
//
//      val form: FormData = FormData(
//        "apiUser" -> mailUser,
//        "apiKey" -> mailKey,
//        "xsmtpapi" -> xsmtpapi,
//        "templateInvokeName" -> template,
//        "from" -> from,
//        "fromName" -> fromName,
//        "subject" -> subject
//      )
//
//      for {
//        response <- Http().singleRequest(
//          HttpRequest(
//            uri = mailUrl,
//            method = HttpMethods.POST,
//            entity = form.toEntity
//          )
//        )
//        entity <- response.entity.toStrict(30.seconds)
//        entityBS <- entity.dataBytes.runFold(ByteString.empty) {
//          case (acc, b) => acc ++ b
//        }
//      } yield {
//
//        if (response.status == StatusCodes.OK) {}
//
//        val pattern = """.*"result":(true|false).*""".r
//        val entityStr = entityBS.utf8String
//        println(s"got entityStr: $entityStr")
//        val pattern(ok) = entityStr
//        println("got result: " + ok)
//        if (ok == "true") true else false
//      }
//    }
//    //  def sendMailTemplateList() = ???
//    //  def sendMailTemplateWithAttachment = ???
//
//}
