package com.yimei.template.services

import com.yimei.template.ApplicationContext
import com.yimei.template.mongo.MongoModels.OssAssetDocument
import org.mongodb.scala.bson.ObjectId

import scala.concurrent.Future

/**
  * Created by hary on 2017/6/22.
  */
class AssetService {

  import ApplicationContext._

  val log = getLogger(this)

  def insertUploadRecord(filename: String, mimeType: String, realname: String): Future[Boolean] = {
    mongoSupport.mongoAsset
      .insertOne(OssAssetDocument(new ObjectId(), filename, mimeType, realname)).toFuture().map { _ => true }
  }

}
