package models.account

import org.joda.time.DateTime

import infrastructure.Mailer
import models.support.ModelFactory
import models.support.Validator
import scalikejdbc.ResultName
import scalikejdbc.WrappedResultSet

case class AccountProvision(
    uuid: Option[String],
    accountId: AccountId,
    entryDate: Option[DateTime] = None) extends Validator with AccountProvisionRepository {

  lazy private val uuidCheck = Check(uuid, "uuid") is require

  private val provisionalMessage =
    """この度は本サービスに登録頂き、誠にありがとうございました。
以下のURLにアクセスして本登録を完了させて下さい。

%s/account/registration?p=%s"""

  /** 本登録用Validation */
  def validateForRegistration = validate(uuidCheck)

  /** 仮登録 */
  def create(accountId: AccountId) = stored(accountId)

  /** メール送信処理 */
  def sendMail(email: String, host: String) = Mailer.sendMail("仮登録完了メール", email, provisionalMessage.format(host, uuid.get))

  /** 仮登録データ検索 */
  def find = findByUuid(uuid.get).filter(_.entryDate.get.plusDays(1).isAfterNow())

  /** 仮登録データ削除 */
  def delete = deleteProvisioning(uuid.get)
}

object AccountProvision extends ModelFactory {
  def apply(implicit data: Map[String, Any]): AccountProvision = {
    AccountProvision(
      getOptValue[String]("p"),
      getOptValue[AccountId]("id").getOrElse(AccountId(0)),
      null
    )
  }

  def apply(ap: ResultName[AccountProvision], rs: WrappedResultSet): AccountProvision = {
    AccountProvision(
      rs.stringOpt(ap.uuid),
      AccountId(rs.long(ap.accountId)),
      rs.jodaDateTimeOpt(ap.entryDate)
    )
  }
}