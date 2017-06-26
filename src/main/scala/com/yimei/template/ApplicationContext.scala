package com.yimei.template

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import com.softwaremill.macwire.wire
import com.yimei.template.mongo.MongoSupport
import com.yimei.template.services.{AssetService, HelloService, WorldService}
import org.flywaydb.core.Flyway
import org.mongodb.scala.MongoClient
import pureconfig.loadConfig
import redis.RedisClient
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by hary on 2017/6/22.
  */

/**
  *  整个应用的执行环境
  */

trait ApplicationConfig {
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
  case class AppConfig( test: Boolean,
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
      System.err(s"loadConfig error ${e.toString}")
      throw new Exception("loadConfig failed")
  }
}

object ApplicationContext extends ApplicationConfig {

  implicit val system: ActorSystem = ActorSystem("jfc-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  // 获取日志
  def getLogger(obj: AnyRef) = Logging(system, obj.getClass)

  // 数据库迁移
  private val flyway = new Flyway()
  val flywayConfig = appConfig.flyway;
  flyway.setDataSource(flywayConfig.url, flywayConfig.username, flywayConfig.password)
  flyway.migrate()

  // slick
  val db = Database.forConfig("slick")
  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)

  // redis
  lazy val redisClient = RedisClient(appConfig.redis.host, appConfig.redis.port)

  // mongodb
  lazy val mongoSupport: MongoSupport = new MongoSupport(MongoClient(appConfig.mongodb.uri), appConfig.mongodb)

  // assemble all the services
  lazy val helloService = wire[HelloService]
  lazy val worldService = wire[WorldService]
  lazy val assetService = wire[AssetService]
}

