package com.yimei.template.context

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorMaterializer
import com.softwaremill.macwire.wire
import com.yimei.template.db.AllTable
import com.yimei.template.mongo.MongoSupport
import com.yimei.template.services.{AssetService, HelloService, WorldService}
import redis.RedisClient

/**
  * Created by hary on 2017/6/22.
  */

/**
  * 整个应用的执行环境
  */

trait GlobalImplicits {
  implicit val system: ActorSystem = ActorSystem("jfc-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
}

object Context extends Config with Migration with GlobalImplicits {

  trait WithLog {
    def log: LoggingAdapter
  }

  // 获取日志
  def getLogger(obj: AnyRef) = Logging(system, obj.getClass)

  // 迁移数据库
  migrate

  // redis  mongodb  db  neo
  lazy val redisClient = RedisClient(appConfig.redis.host, appConfig.redis.port)
  lazy val mongoSupport = wire[MongoSupport]
  lazy val slickdb = wire[AllTable]

  // assemble all the services
  lazy val helloService = wire[HelloService]
  lazy val worldService = wire[WorldService]
  lazy val assetService = wire[AssetService]
}

