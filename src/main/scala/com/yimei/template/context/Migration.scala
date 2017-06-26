package com.yimei.template.context

import org.flywaydb.core.Flyway

/**
  * Created by hary on 2017/6/26.
  */

// 数据库迁移
trait Migration { self: Config =>

  private[this] val flyway = new Flyway()
  flyway.setDataSource(appConfig.flyway.url, appConfig.flyway.username, appConfig.flyway.password)

  // 迁移
  def migrate = {
    flyway.migrate()
    this
  }

  // 清除数据库
  def drop = {
    flyway.clean()
    this
  }
}
