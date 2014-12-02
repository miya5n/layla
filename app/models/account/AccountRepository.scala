package models.account

import scalikejdbc._
import org.joda.time.DateTime

trait AccountRepository extends SQLSyntaxSupport[Account] {
  override val tableName = "account"
  val a = this.syntax("a")

  def authenticate(email: String, password: String)(implicit session: DBSession = autoSession): Option[Account] = {
    withSQL {
      select.from(this as a).where.eq(a.email, email).and.eq(a.password, password)
    }.map {
      Account(a.resultName, _)
    }.single.apply()
  }

  // TODO 仮登録がいる
  def save(account: Account)(implicit session: DBSession = autoSession) = {
    withSQL {
      insert.into(this).values(
        0,
        account.email,
        account.name,
        account.password,
        account.sex,
        account.age,
        2,
        DateTime.now,
        DateTime.now)
    }.update.apply()
  }
}