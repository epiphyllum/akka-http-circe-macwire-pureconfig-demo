package com.yimei.template.mongo

import com.yimei.template.ApplicationContext.MongoDBConfig
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.Macros
import org.mongodb.scala.{MongoClient, MongoCollection}

/**
  * Created by hary on 2017/6/22.
  */
class MongoSupport(mongoClient: MongoClient, mongo: MongoDBConfig) {

  import MongoModels._

  private[this] val codecRegistry = fromRegistries(
    fromProviders(
      Macros.createCodecProviderIgnoreNone[OssAssetDocument]()
    )
  )

  val mongoDb = mongoClient.getDatabase(mongo.database).withCodecRegistry(codecRegistry)
  val mongoAsset: MongoCollection[OssAssetDocument] = mongoDb.getCollection("asset")
}
