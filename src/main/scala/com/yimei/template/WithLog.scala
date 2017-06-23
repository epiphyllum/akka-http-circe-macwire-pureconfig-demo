package com.yimei.template

import akka.event.LoggingAdapter

/**
  * Created by hary on 2017/6/23.
  */
trait WithLog {
  def log: LoggingAdapter
}
