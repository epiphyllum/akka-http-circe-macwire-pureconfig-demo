package com.yimei.template.db

import java.sql.Timestamp
import java.sql.Date
import java.time.{LocalDate, LocalDateTime}

import com.yimei.template.dict.CompanyType
import slick.lifted.MappedTo
import slick.jdbc.MySQLProfile.api._

/**
  * Created by hary on 2017/6/28.
  */
object CustomColumn {
  case class Money(value: Long) extends AnyVal with MappedTo[Long]
  case class Salary(value: Long) extends AnyVal with MappedTo[Long] {
    def custFun = ???
  }

  implicit val localDateToDate = MappedColumnType.base[LocalDate, Date](
    l => Date.valueOf(l),
    d => d.toLocalDate
  )

  implicit val JavaLocalDateTimeMapper = MappedColumnType.base[LocalDateTime, Timestamp](
    l => Timestamp.valueOf(l),
    t => t.toLocalDateTime
  )

  // 枚举类的映射
  import com.yimei.template.dict.CompanyType.CompanyType
  implicit val companTypeMapper = MappedColumnType.base[CompanyType, Int](
    ct => ct.id,
    id => CompanyType(id)
  )
}
