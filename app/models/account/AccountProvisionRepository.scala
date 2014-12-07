package models.account

import org.joda.time.DateTime

import scalikejdbc.DBSession
import scalikejdbc.SQLSyntaxSupport
import scalikejdbc.delete
import scalikejdbc.insert
import scalikejdbc.select
import scalikejdbc.withSQL

trait AccountProvisionRepository extends SQLSyntaxSupport[AccountProvision] {
  override val tableName = "account_provision"
  val ap = this.syntax("ap")

  def findByUuid(uuid: String)(implicit session: DBSession = autoSession): Option[AccountProvision] = {
    withSQL {
      select.from(this as ap).where.eq(ap.uuid, uuid)
    }.map {
      AccountProvision(ap.resultName, _)
    }.single.apply()
  }

  def stored(accountId: AccountId)(implicit session: DBSession = autoSession) = {
    val provision = AccountProvision(Some(java.util.UUID.randomUUID.toString), accountId, Some(DateTime.now))
    withSQL {
      insert.into(this).values(
        provision.uuid,
        provision.accountId.value,
        provision.entryDate.get)
    }.update.apply()
    provision
  }

  def deleteProvisioning(uuid: String)(implicit session: DBSession = autoSession) = {
    withSQL {
      delete.from(this).where.eq(this.column.uuid, uuid)
    }.update.apply()
  }
}