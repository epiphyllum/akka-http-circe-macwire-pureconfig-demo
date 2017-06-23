package com.yimei.template.routes.captcha

import java.io.ByteArrayOutputStream
import java.util.Properties

import akka.NotUsed
import akka.http.scaladsl.model.ContentTypes.`application/octet-stream`
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.model.headers.{ContentDispositionTypes, `Content-Disposition`}
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.yimei.template.http.ExceptionConfig.DatabaseException

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by hary on 2017/6/23.
  */
trait GetCaptchaController {

  import com.yimei.template.ApplicationContext._

  // val log = getLogger(this)

  import javax.imageio.ImageIO

  import com.google.code.kaptcha.util.Config;
  ImageIO.setUseCache(false);

  /**
    *
    * @param ec
    * @return
    */
  def genRecaptcha(implicit ec: ExecutionContext): (Source[ByteString, NotUsed], String) = {
    val randomCode = "%06d".format(math.abs((new util.Random).nextInt(999999)))
    val source = Source.fromFuture(Future {
      val props = new Properties();
      props.put("kaptcha.border", "no");
      props.put("kaptcha.textproducer.font.color", "black");
      props.put("kaptcha.textproducer.char.space", "4");
      props.put("kaptcha.textproducer.char.length", "6");
      val config = new Config(props);
      val producer = config.getProducerImpl();
      val bufferedImage = producer.createImage(randomCode)
      val baos = new ByteArrayOutputStream()
      ImageIO.write(bufferedImage, "jpg", baos)
      ByteString(baos.toByteArray)
    })

    (source, randomCode)
  }

  // 获取验证码
  def handleGetCaptcha(clientId: String): Future[HttpResponse] = {
    val (source, code) = genRecaptcha
    // log.info(s"生成的随机图片验证码为: $code")
    redisClient.setex(s"$clientId:kap", 1000, code) map {
      if (_) {
        HttpResponse(
          status = StatusCodes.OK,
          headers = List(`Content-Disposition`(ContentDispositionTypes.inline)),
          entity = HttpEntity(`application/octet-stream`, source)
        )
      } else {
        throw DatabaseException("can not save recaptha to redis")
      }
    }
  }


}
