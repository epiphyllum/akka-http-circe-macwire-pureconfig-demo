package com.yimei.template

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import com.softwaremill.macwire.wire
import com.yimei.template.mongo.MongoSupport
import com.yimei.template.services.{AssetService, HelloService}
import org.mongodb.scala.MongoClient
import pureconfig.loadConfig
import redis.RedisClient

/**
  * Created by hary on 2017/6/22.
  */

/**
  *  整个应用的执行环境
  */
object ApplicationContext {

  /**
    * Created by hary on 2017/6/22.
    */
  case class RedisConfig(host: String, port: Int, password: String)
  case class MongoDBConfig(uri: String, database: String)
  case class NeoConfig(host: String, port: Int, username: String, password: String)
  case class AliyunConfig(accessKeyId: String, accessKeySecret: String, ossBucket: String, ossHost: String)
  case class SmsTemplate(id: String, vars: Map[String, String])
  case class SmsConfig(smsUser: String, smsKey: String, smsUrl: String, templates: Map[String, SmsTemplate])
  case class SendcloudConfig(sms: SmsConfig)
  case class ServerConfig(domain: String, port: Int)
  case class JwtConfig(jwtSecret: String, jwtExpire: Long)
  case class AppConfig( test: Boolean,
                        initPassword: String,
                        redis: RedisConfig,
                        mongodb: MongoDBConfig,
                        neo: NeoConfig,
                        aliyun: AliyunConfig,
                        sendcloud: SendcloudConfig,
                        server: ServerConfig,
                        jwt: JwtConfig
                      )

  implicit val system: ActorSystem = ActorSystem("jfc-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  def getLogger(obj: AnyRef) = Logging(system, obj.getClass)

  // 应用配置
  val appConfig: AppConfig = loadConfig[AppConfig] match {
    case Right(b) => b
    case Left(e) =>
      getLogger(this.getClass).error(s"loadConfig error ${e.toString}")
      throw new Exception("loadConfig failed")
  }

  println(appConfig)

  // redis
  val redisClient = RedisClient(appConfig.redis.host, appConfig.redis.port)

  // mongodb
  val mongoSupport: MongoSupport = new MongoSupport(MongoClient(appConfig.mongodb.uri), appConfig.mongodb)

  // assemble all the services
  lazy val helloService = wire[HelloService]
  lazy val assetService = wire[AssetService]
}

