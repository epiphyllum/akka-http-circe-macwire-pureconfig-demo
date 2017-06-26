package com.yimei.template.mongo

import com.yimei.template.context.Context._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.Macros
import org.mongodb.scala.{MongoClient, MongoCollection}

/**
  * Created by hary on 2017/6/22.
  */
class MongoSupport {

  import MongoModels._

  private[this] val mongoDb = {
    val codecRegistry = fromRegistries(
      fromProviders(
        Macros.createCodecProviderIgnoreNone[OssAssetDocument]()
      )
    )

    val mongoClient = MongoClient(appConfig.mongodb.uri);
    mongoClient.getDatabase(appConfig.mongodb.database).withCodecRegistry(codecRegistry)
  }
  val mongoAsset: MongoCollection[OssAssetDocument] = mongoDb.getCollection("asset")
}
