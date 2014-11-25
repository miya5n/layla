package models.support

trait EnumLike {
  type ValueType

  def value: ValueType
}

trait StringType extends EnumLike {
  type ValueType = String
}

trait ShortType extends EnumLike {
  type ValueType = Short
}

trait EnumCompanion[A <: EnumLike] {
  def values: Seq[A]

  def valueOf(value: A#ValueType): Option[A] = values.find(_.value == value)
}