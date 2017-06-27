package com.yimei.macros
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

/**
  * Created by hary on 2017/6/26.
  */
class CirceEnum extends scala.annotation.StaticAnnotation {
  def macroTransfrom(annottees: Any*) = macro  CirceEnum.impl
}

object CirceEnum {
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val result = {
      annottees.map(_.tree).toList match {
        case q"$mods object $tname extends { ..$earlydefns } with ..$parents { $self => ..$body }" :: Nil =>

          println("match here!!!")
          // val typeStr  = module.symbol.name
//          println(s"match here got typeStr: $typeStr")

//          val encodeName: TermName = c.freshName(TermName(typeStr + "Encoder"))
//          val decodeName: TermName = c.freshName(TermName(typeStr + "Decoder"))

//          q"""
//             object $module extends Enumeration with ..$parents{ $self =>
//               val codec = CirceEnum[Value][$module]
//               implicit val $encodeName = codec.encoder
//               implicit val $decodeName = codec.decoder
//             }
//           """

        case _ =>  c.abort(c.enclosingPosition, "Annotation @CirceEnum can only used in Enumeration")
      }
    }

    annottees(0)

  }
}
