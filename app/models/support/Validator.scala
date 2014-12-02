package models.support

/**
 * 共通Validate
 * TODO 要テスト
 * TODO すごくDRY感満載
 * TODO 要リファクタリング
 */
trait Validator {

  def validate[V](checkList: Seq[Check[_]], model: V) = checkList.flatMap(_.getErrors) match {
    case Nil   => Right(model)
    case error => Left(error)
  }

  case class Check[T](
      item: Option[T],
      itemName: String,
      min: Int = 0,
      max: Int = 0,
      private val errorMessage: Option[String] = None) {

    def is(func: Check[T] => Either[String, Check[T]]) = errorMessage match {
      case None => func(this) match {
        case Left(message) => this.copy(errorMessage = Some(message))
        case Right(check)  => check
      }
      case _ => this
    }

    def isEqual(target: Check[T]) = (target.item == this.item) match {
      case true  => this
      case false => this.copy(errorMessage = Some(s"${target.itemName}と${this.itemName}が一致していません。"))
    }

    def and(func: Check[T] => Either[String, Check[T]]) = is(func)

    def getErrors = errorMessage
  }

  def require[T](target: Check[T]) = target.item match {
    case None                                 => Left(s"${target.itemName}を指定してください。")
    case Some(item: String) if item.isEmpty() => Left(s"${target.itemName}を指定してください。")
    case _                                    => Right(target)
  }

  // TODO 他の型もいずれは・・・
  def minLength[T](target: Check[T]) = target.item match {
    case Some(item: String) if (item.size >= target.min) => Right(target)
    case Some(item: Int) if (item >= target.min) => Right(target)
    case Some(item: Long) if (item >= target.min) => Right(target)
    case _ => Left(s"${target.itemName}は${target.min}以上を指定してください。")
  }

  def maxLength[T](target: Check[T]) = target.item match {
    case Some(item: String) if (item.size <= target.max) => Right(target)
    case Some(item: Int) if (item <= target.max) => Right(target)
    case Some(item: Long) if (item <= target.max) => Right(target)
    case _ => Left(s"${target.itemName}は${target.max}以下を指定してください。")
  }

  def range[T](target: Check[T]) = target.item match {
    case Some(item: String) if (item.size >= target.min && item.size <= target.max) => Right(target)
    case Some(item: Int) if (item >= target.min && item <= target.max) => Right(target)
    case Some(item: Long) if (item >= target.min && item <= target.max) => Right(target)
    case _ => Left(s"${target.itemName}は指定範囲を超えています。")
  }

  private val regex = "^([*+!.&#$|\\'\\\\%\\/0-9a-zA-Z^_`{}=?~:-]+)@(([0-9a-zA-Z-]+\\.)+[0-9a-zA-Z]{2,})$".r
  def Email[T](target: Check[T]) = {
    target.item match {
      case Some(item: String) if (regex.findFirstIn(item) != None) => Right(target)
      case _ => Left(s"${target.itemName}の形式が間違っています。")
    }
  }
}