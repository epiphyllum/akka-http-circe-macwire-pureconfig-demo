package com.yimei.template.services
import com.yimei.template.context.Context._
/**
  * Created by hary on 2017/6/23.
  */
class WorldService(helloService: HelloService) {

  case class MergeCase(appConfig: AppConfig, sms: SmsConfig)
  def getMergeCase = MergeCase(helloService.getAppConfig, appConfig.sendcloud.sms)
}
