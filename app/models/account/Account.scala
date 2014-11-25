package models.account

import infrastructure.Entity
import scala.reflect.runtime.universe

case class Account(
    id: AccountId,
    name: Option[String],
    sex: Option[SexType],
    age: Option[Int],
    email: Option[String],
    pass: Option[String],
    passConfirm: Option[String]) extends Entity[AccountId] with Validator {

  lazy val nameCheck = check(name) is require
  lazy val sexCheck = check(sex) is require
  lazy val ageCheck = check(age) is require and range(1 to 99)
  lazy val emailCheck = check(email) is require and isEmail
  lazy val passCheck = check(pass) is require
  lazy val passConfirmCheck = check(passConfirm) is require

  /**
   * TODO とりあえず実装
   */
  def validateForEntry = {
    //    check(name) 
  }
}

/**
 * TODO 共通のとこに移動する
 */
trait Validator {
  var target: Option[_] = _

  def check[T](data: Option[T]): Validator = {
    target = data
    this
  }

  def is(func: => Validator): Validator = func
  def and(func: => Validator): Validator = func

  def require: Validator = {
    target match {
      case None => throw new Exception("指定されていません。")
      case _    => this
    }
  }

  def minLength(min: Int): Validator = {
    target.get match {
      case x: String if (x.size >= min) => this
      case _                            => throw new Exception
    }
  }

  def maxLength(max: Int): Validator = {
    target.get match {
      case x: String if (x.size <= max) => this
      case _                            => throw new Exception
    }
  }

  def range(minToMax: Range): Validator = {
    target.get match {
      case x: Int if (x >= minToMax.head && x <= minToMax.last) => this
      case _ => throw new Exception("指定範囲を超えています。")
    }
  }

  def isEmail: Validator = {
    val regex = """/^([a-zA-Z0-9])+([a-zA-Z0-9\._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9\._-]+)+$/""".r
    target.get match {
      case regex(value) => this
      case _            => throw new Exception("Emailの形式が間違ってるわ！")
    }
  }

}

object Account {

  /**
   * TODO 別のクラスに移動する/まだ修正の余地あり value(0)はおかしい/例外処理もいる
   */
  def getOptValue[T](key: String)(implicit data: Map[String, Seq[String]]): Option[T] = {
    data.get(key) match {
      case Some(value) => Some(value(0).trim().asInstanceOf[T])
      case None        => None
    }
  }

  def apply(implicit data: Map[String, Seq[String]]): Account = {
    Account(
      getOptValue[AccountId]("id").getOrElse(AccountId(0)),
      getOptValue[String]("name"),
      getOptValue[SexType]("sex"),
      getOptValue[Int]("age"),
      getOptValue[String]("email"),
      getOptValue[String]("pass.main"),
      getOptValue[String]("pass.confirm")
    )
  }

}