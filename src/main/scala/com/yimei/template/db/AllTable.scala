package com.yimei.template.db
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by hary on 2017/6/26.
  */
class AllTable extends CertificateTable {
  val slickdb = Database.forConfig("slick")
  def exec[T](program: DBIO[T]): T = Await.result(slickdb.run(program), 2 seconds)
}
