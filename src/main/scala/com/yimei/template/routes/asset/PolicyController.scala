package com.yimei.template.routes.asset

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import com.yimei.template.http.ExtensionDirectives._
import javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA1

import com.yimei.template.WithLog

/**
  * Created by hary on 2017/6/23.
  */
trait PolicyController extends WithLog {

  import com.yimei.template.ApplicationContext._

  // 获取上传文件签名
  def hmacSHA1(key: Array[Byte], data: Array[Byte]): String = {
    val signingKey: SecretKeySpec = new SecretKeySpec(key, HMAC_SHA1);
    val mac = Mac.getInstance("HmacSHA1");
    mac.init(signingKey);
    val rawHmac = mac.doFinal(data);
    base64Encode(rawHmac);
  }

  def base64Encode(origin: Array[Byte]): String = {
    if (null == origin) {
      return null;
    }
    new sun.misc.BASE64Encoder().encode(origin).replace("\n", "").replace("\r", "");
  }

  val callbackBody = "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}&realname=${x:realname}";
  val callback = base64Encode(
    s"""
       |{
       |"callbackUrl":"http://${appConfig.server.domain}/api/asset/callback",
       |"callbackBody":"$callbackBody",
       |"callbackBodyType":"application/x-www-form-urlencoded"
       |}
    """.stripMargin.getBytes()
  )

  private[this] def getPolicy: String = {
    val expire = "2120-01-01T12:00:00.000Z" // todo: should be now + 10 minutes
    s"""
       |{
       |"expiration": "$expire",
       |"conditions": [
       |  ["content-length-range", 0, 1073741824]
       |]
       |}
    """.stripMargin
  }

  case class PolicyResponse(callback: String, signature: String, policy: String, OSSAccessKeyId: String, host: String, dirName: String)

  def handleUploadPolicy(dirname: String): Result[PolicyResponse] = {
    val policy = base64Encode(getPolicy.getBytes());
    ok(
      PolicyResponse(
        callback,
        hmacSHA1(appConfig.aliyun.accessKeySecret.getBytes(), policy.getBytes()),
        policy,
        appConfig.aliyun.accessKeyId,
        appConfig.aliyun.ossHost,
        dirname)
    )
  }
}
