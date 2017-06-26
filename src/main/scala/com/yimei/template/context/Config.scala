package com.yimei.template.context

import pureconfig.loadConfig

/**
  * Created by hary on 2017/6/26.
  */
trait Config {
  /**
    * Created by hary on 2017/6/22.
    */
  case class RedisConfig(host: String, port: Int, password: String)

  case class MongoDBConfig(uri: String, database: String)

  case class FlywayConfig(username: String, password: String, url: String)

  case class NeoConfig(host: String, port: Int, username: String, password: String)

  case class AliyunConfig(accessKeyId: String, accessKeySecret: String, ossBucket: String, ossHost: String)

  case class SmsTemplate(id: String, vars: Map[String, String])

  case class SmsConfig(smsUser: String, smsKey: String, smsUrl: String, templates: Map[String, SmsTemplate])

  case class SendcloudConfig(sms: SmsConfig)

  case class ServerConfig(domain: String, port: Int)

  case class JwtConfig(secret: String, expire: Long)

  case class AppConfig(test: Boolean,
                       initPassword: String,
                       redis: RedisConfig,
                       flyway: FlywayConfig,
                       mongodb: MongoDBConfig,
                       neo: NeoConfig,
                       aliyun: AliyunConfig,
                       sendcloud: SendcloudConfig,
                       server: ServerConfig,
                       jwt: JwtConfig
                      )


  // 应用配置
  val appConfig: AppConfig = loadConfig[AppConfig] match {
    case Right(b) => b
    case Left(e) =>
      System.err.println(s"loadConfig error ${e.toString}")
      throw new Exception("loadConfig failed")
  }

}

