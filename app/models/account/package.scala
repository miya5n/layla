package models

import models.support.{ EnumCompanion, ShortType }

package object account {

  sealed abstract class SexType(val value: Short) extends ShortType
  object SexType extends EnumCompanion[SexType] {
    case object MAN extends SexType(1)
    case object WOMAN extends SexType(2)
    lazy val values: Seq[SexType] = Seq(MAN, WOMAN)
  }
}
