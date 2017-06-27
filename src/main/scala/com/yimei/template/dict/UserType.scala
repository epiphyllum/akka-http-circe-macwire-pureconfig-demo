package com.yimei.template.dict
// import com.yimei.macros.CirceEnum

/**
  * Created by hary on 2017/6/26.
  */

// @CirceEnum
object UserType extends Enumeration {

  type UserType = Value

  val SOE_CAP_FULL = Value(0, "国有上市企业全资控股企业")
  val SOE_CAP_ABS = Value(1, "国有上市企业绝对控股企业")
  val SOE_CAP_REL = Value(2, "国有上市企业相对控股企业")

  val SOE_NON_FULL = Value(3, "国有非上市企业全资控股企业")
  val SOE_NON_ABS = Value(4, "民营上市企业全资控股企业")
  val SOE_NON_REL = Value(5, "民营上市企业全资控股企业")

  val PRI_CAP_FULL = Value(6, "民营上市企业全资控股企业")
  val PRI_CAP_ABS = Value(7, "民营上市企业绝对控股企业")
  val PRI_CAP_REL = Value(8, "民营上市企业全资控股企业")

  val PRI_NON = Value(9, "民营非上市企业")
}
