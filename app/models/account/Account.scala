package models.account

import models.infrastructure.Entity
import models.support.ModelFactory
import models.support.Validator
import scalikejdbc.ResultName
import scalikejdbc.ResultName
import scalikejdbc.WrappedResultSet

case class Account(
    id: AccountId,
    name: Option[String],
    sex: Option[SexType],
    age: Option[Int],
    email: Option[String],
    password: Option[String], // TODO 暗号化にしないと！
    passConfirm: Option[String] = None) extends Entity[AccountId] with Validator {

  lazy val idCheck = Check(Some(id), "id") is require
  lazy val nameCheck = Check(name, "名前") is require
  lazy val sexCheck = Check(sex, "性別") is require
  lazy val ageCheck = Check(age, "年齢", min = 1, max = 99) is require and range
  lazy val emailCheck = Check(email, "Eメール") is require and Email
  lazy val passCheck = Check(password, "パスワード") is require
  lazy val confirmCheck = Check(passConfirm, "確認用パスワード") is require
  lazy val passAndConfirmCheck = Check(password, "パスワード") isEqual Check(passConfirm, "確認用パスワード")

  /**
   * 認証処理用Validation
   */
  def validateForLogin: Either[Seq[String], Account] =
    validate(Seq(emailCheck, passCheck), this)

  /**
   * 登録処理用Validation
   */
  def validateForEntry: Either[Seq[String], Account] =
    validate(Seq(nameCheck, sexCheck, ageCheck, emailCheck, passCheck, confirmCheck, passAndConfirmCheck), this)
}

/** Account用のFactoryオブジェクト  */
object Account extends ModelFactory {

  def apply(implicit data: Map[String, Seq[String]]): Account = {
    Account(
      getOptValue[AccountId]("id").getOrElse(AccountId(0)),
      getOptValue[String]("name"),
      getOptValue[SexType]("sex"),
      getOptValue[Int]("age"),
      getOptValue[String]("email"),
      getOptValue[String]("pass"),
      getOptValue[String]("pass.confirm")
    )
  }

  // TODO Scalikeの型がいるのが嫌だ
  def apply(a: ResultName[Account], rs: WrappedResultSet): Account = {
    val d = rs.short(a.sex)
    Account(
      AccountId(rs.long(a.id)),
      rs.stringOpt(a.name),
      Some(SexType.MAN), // => TODO どうしよう
      rs.intOpt(a.age),
      rs.stringOpt(a.email),
      rs.stringOpt(a.password)
    )
  }
}