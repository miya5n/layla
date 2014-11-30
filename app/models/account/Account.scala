package models.account

import models.infrastructure.Entity
import scala.util.Success
import scala.util.Failure
import models.support.Validator
import scalikejdbc.WrappedResultSet
import scalikejdbc.ResultName
import org.hamcrest.core.IsEqual

case class Account(
    id: AccountId,
    name: Option[String],
    sex: Option[SexType],
    age: Option[Int],
    email: Option[String],
    pass: Option[String],
    passConfirm: Option[String] = None) extends Entity[AccountId] with Validator {

  lazy val idCheck = Check(Some(id), "id") is require
  lazy val nameCheck = Check(name, "名前") is require
  lazy val sexCheck = Check(sex, "性別") is require
  lazy val ageCheck = Check(age, "年齢", min = 1, max = 99) is require and range
  lazy val emailCheck = Check(email, "Eメール") is require and Email
  lazy val passCheck = Check(pass, "パスワード") is require
  lazy val confirmCheck = Check(passConfirm, "確認用パスワード") is require
  lazy val passAndConfirmCheck = Check(pass, "パスワード") isEqual Check(passConfirm, "確認用パスワード")

  /**
   * 登録処理用Validation
   */
  def validateForEntry: Either[Seq[String], Account] = {
    Seq(idCheck, nameCheck, sexCheck, ageCheck, emailCheck, passCheck, confirmCheck, passAndConfirmCheck).flatMap(_.hasError) match {
      case error if (error.isEmpty) => Right(this)
      case error                    => Left(error)
    }
  }
}

/**
 * Account用のFactoryオブジェクト
 */
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

  // TODO Scalikeの型がいるのが嫌だ
  def apply(a: ResultName[Account], rs: WrappedResultSet): Account = {
    val d = rs.short(a.sex)
    Account(
      AccountId(rs.long(a.id)),
      rs.stringOpt(a.name),
      Some(SexType.MAN),
      rs.intOpt(a.age),
      rs.stringOpt(a.email),
      rs.stringOpt(a.pass)
    )
  }
}