package com.yimei.template.routes.mock

import com.wix.accord.dsl._
import com.yimei.template.dict.CirceEnumCodec
import com.yimei.template.http.ExtensionDirectives._
import io.circe._
import nl.grons.metrics.scala.DefaultInstrumented

import scala.concurrent.Future
import scala.util.Try

/**
  * Created by hary on 2017/6/23.
  */
trait MockController extends DefaultInstrumented {

  case class ControllerRequest(message: String) {

    // 这里是请求校验
    def validate(): (Boolean, String) = genValidator(this) {
      validator[ControllerRequest] { mr =>
        mr.message must startWith("error")
      }
    }

    // 这里是业务检查, 函数签名只有一个要求， boolean + string 的tuple
    def validate2(): (Boolean, String) = {
      // 查询数据库， 检验用户情况 etc...
      (false, "business validation failed")
    }

    // 这是业务控制检查
    def authorize = {
      Future.successful(false)
    }
  }

  case class ControllerResponse(message: String)

  def handlePost(req: ControllerRequest) = {
    fok(Future.successful(ControllerResponse(req.message + " echo back")))
  }


  object CompanyType extends Enumeration {

    type CompanyType = Value

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

    //    implicit val companyTypeEnc: Encode[CompanyType] = Encoder.forProduct2("id", "value"){
    //      (src: Source_) => src.
    //    }

  }
  import CompanyType._
  private[this] val codec = CirceEnumCodec[CompanyType](CompanyType)
  implicit val companyTypeEncoder = codec.encoder
  implicit val companyTypeDecoder = codec.decoder


//  val (companyTypeEncoder, companyTypeDecoder) = {
//    val vmap = CompanyType.values.toList.map { v => (v.id, v) }.toMap
//    import cats.syntax.either._
//
//
//    val encoder: Encoder[CompanyType.CompanyType] = new Encoder[CompanyType.CompanyType] {
//      override final def apply(a: CompanyType.CompanyType): Json = Json.obj(
//        ("id", Json.fromInt(a.id)),
//        ("value", Json.fromString(a.toString))
//      )
//    }
//
//    val decoder: Decoder[CompanyType.CompanyType] = Decoder.decodeInt.emap { mid =>
//      Either.catchNonFatal(vmap(mid)).leftMap(t => "companyType")
//    }
//    (encoder, decoder)
//  }

  case class HasEnum(companyType: CompanyType.CompanyType, name: String)


  def handleEnum() = {
    import CompanyType._
    ok(HasEnum(SOE_CAP_ABS, "hary"))
  }

}
