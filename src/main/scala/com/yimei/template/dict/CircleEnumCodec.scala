package com.yimei.template.dict

import io.circe.{Decoder, Encoder, Json}


/**
  * Created by hary on 2017/6/26.
  */
object CirceEnumCodec {

  import cats.syntax.either._

  case class EnumCodec[T](encoder: Encoder[T], decoder: Decoder[T])

  def apply[T <: Enumeration#Value](enum: Enumeration): EnumCodec[T] = {
    val encoder = new Encoder[T] {
      override final def apply(a: T): Json = Json.obj(
        ("id", Json.fromInt(a.id)),
        ("value", Json.fromString(a.toString))
      )
    }

    // val vmap =  enum.values.toList.map { v => (v.id, v.asInstanceOf[T]) }.toMap

    val decoder : Decoder[T] = Decoder.decodeInt.emap { mid =>
      Either.catchNonFatal(enum(mid).asInstanceOf[T]).leftMap(t => enum.toString())
    }

    EnumCodec(encoder, decoder)
  }


}
