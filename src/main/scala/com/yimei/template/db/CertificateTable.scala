package com.yimei.template.db
import slick.jdbc.MySQLProfile.api._

/**
  * Created by hary on 2017/6/26.
  */
trait CertificateTable {

  case class CertificateEntity(id:Option[Int],idType:Int,idName:String)
  class Certificate(tag:Tag) extends Table[CertificateEntity](tag,"certificate"){
    def id = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
    def idType = column[Int]("idType")
    def idName = column[String]("idName")
    def * = (id, idType, idName) <> (CertificateEntity.tupled, CertificateEntity.unapply)
  }
  protected val certificate = TableQuery[Certificate]
}
