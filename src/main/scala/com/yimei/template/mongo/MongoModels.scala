package com.yimei.template.mongo

import org.mongodb.scala.bson.ObjectId

/**
  * Created by hary on 2017/6/22.
  */
object MongoModels {
  case class OssAssetDocument(_id: ObjectId, filePath: String, mimeType: String, realname: String)
  case class FileDocument(filePath: String, fileName: String, fileExtName: String)
}
