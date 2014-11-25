package infrastructure

trait Identifier[+A] {

  def id: A

  val isDefined: Boolean = true

  val isEmpty: Boolean = !isDefined

  override def equals(obj: Any) = obj match {
    case that: Identifier[_] => id == that.id
    case _                   => false
  }

  override def hashCode = 31 * id.##
}

object EmptyIdentifier extends EmptyIdentifier

trait EmptyIdentifier extends Identifier[Nothing] {

  def id: Nothing = throw new NoSuchElementException

  override val isDefined = false

  override def equals(obj: Any) = obj match {
    case that: EmptyIdentifier => this eq that
    case _                     => false
  }

  override def hashCode = 0
}