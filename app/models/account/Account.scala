package models.account

import models.infrastructure.Entity
import models.support.ModelFactory
import models.support.Validator
import scalikejdbc.ResultName
import scalikejdbc.WrappedResultSet

case class Account(
    id: AccountId,
    name: Option[String],
    sex: Option[Int],
    age: Option[Int],
    email: Option[String],
    password: Option[String],
    passConfirm: Option[String] = None,
    provisionalInfo: AccountProvision) extends Entity[AccountId] with Validator with AccountRepository {

  lazy private val accountIdCheck = Check(Some(id), "id") is require
  lazy private val nameCheck = Check(name, "名前") is require
  lazy private val sexCheck = Check(sex, "性別") is require
  lazy private val ageCheck = Check(age, "年齢", min = 1, max = 99) is require and range
  lazy private val emailCheck = Check(email, "Eメール") is require and Email
  lazy private val passCheck = Check(password, "パスワード") is require
  lazy private val confirmCheck = Check(passConfirm, "確認用パスワード") is require
  lazy private val passAndConfirmCheck = Check(password, "パスワード") isEqual Check(passConfirm, "確認用パスワード")
  lazy private val notExistEmailCheck = Check(findByEmail(email.get), "Eメール") is notExists

  /** 認証処理用Validation */
  def validateForLogin: Either[Seq[String], Account] = {
    validate(emailCheck, passCheck) match {
      case Nil =>
        authenticate(email.get, password.get) match {
          case None          => diagnosis(Seq("メールアドレス、パスワードが間違っています。"), this)
          case Some(account) => diagnosis(Nil, account)
        }
      case errors => diagnosis(errors, this)
    }
  }

  /** 仮登録処理用Validation */
  def validateForProvisioning: Either[Seq[String], Account] = {
    validate(nameCheck, sexCheck, ageCheck, emailCheck, passCheck, confirmCheck, passAndConfirmCheck, notExistEmailCheck) match {
      case Nil    => diagnosis(Nil, this)
      case errors => diagnosis(errors, this)
    }
  }

  /** 登録処理用Validation */
  def validateForEntry: Either[Seq[String], Account] = {
    provisionalInfo.validateForRegistration match {
      case Nil => provisionalInfo.find match {
        case None       => diagnosis(Seq("データが存在しません。"), this)
        case Some(data) => diagnosis(Nil, this.copy(id = data.accountId, provisionalInfo = data))
      }
      case errors => diagnosis(errors, this)
    }
  }

  /** 仮登録 */
  def provisioning(protocol: String) = provisionalInfo create (provisionalStored(this)) sendMail (email.get, protocol)

  /** 本登録 */
  def registration = {
    updateForRegistration(id)
    provisionalInfo.delete
  }
}

/** Account用のFactoryオブジェクト  */
object Account extends ModelFactory {
  def apply(implicit data: Map[String, Seq[String]]): Account =
    Account(
      getOptValue[AccountId]("id").getOrElse(AccountId(0)),
      getOptValue[String]("name"),
      getOptValue[Int]("sex"),
      getOptValue[Int]("age"),
      getOptValue[String]("email"),
      getOptValue[String]("pass"),
      getOptValue[String]("pass.confirm"),
      AccountProvision.apply
    )

  def apply(a: ResultName[Account], rs: WrappedResultSet): Account = {
    val accountId = AccountId(rs.long(a.id))
    Account(
      accountId,
      rs.stringOpt(a.c("name")),
      rs.intOpt(a.c("sex")),
      rs.intOpt(a.c("age")),
      rs.stringOpt(a.c("email")),
      rs.stringOpt(a.c("password")),
      None,
      AccountProvision(None, accountId, null)
    )
  }
}