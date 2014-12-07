package models.account

import org.joda.time.DateTime

import scalikejdbc._

trait AccountRepository extends SQLSyntaxSupport[Account] {
  override val tableName = "account"
  override val columns = Seq("id", "email", "name", "password", "sex", "age", "status", "provisioning")
  val a = this.syntax("a")

  def findByEmail(email: String)(implicit session: DBSession = autoSession): Option[Account] = {
    withSQL {
      select.from(this as a).where.eq(a.c("email"), email)
    }.map {
      Account(a.resultName, _)
    }.single.apply()
  }

  def authenticate(email: String, password: String)(implicit session: DBSession = autoSession): Option[Account] = {
    withSQL {
      select
        .from(this as a)
        .where.eq(a.c("email"), email)
        .and.eq(a.c("password"), password)
        .and.eq(a.c("provisioning"), REGIST_STATE.DEFINITIVE)
    }.map {
      Account(a.resultName, _)
    }.single.apply()
  }

  def provisionalStored(account: Account)(implicit session: DBSession = autoSession) = {
    AccountId(withSQL {
      insert.into(this).values(
        0,
        account.email,
        account.name,
        account.password,
        account.sex,
        account.age,
        2, // TODO 定数化
        REGIST_STATE.PROVISIONAL,
        DateTime.now,
        DateTime.now)
    }.updateAndReturnGeneratedKey.apply())
  }

  def updateForRegistration(id: AccountId)(implicit session: DBSession = autoSession) = {
    withSQL {
      update(this as a)
        .set(a.c("provisioning") -> REGIST_STATE.DEFINITIVE)
        .where.eq(a.c("id"), id.value)
    }.update.apply()
  }
}