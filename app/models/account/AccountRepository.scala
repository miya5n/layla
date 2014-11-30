package models.account

import scalikejdbc._

object AccountRepository extends SQLSyntaxSupport[Account] {
  override val tableName = "account"
  val a = this.syntax("a")

  def authenticate(email: String, password: String)(implicit session: DBSession = autoSession): Option[Account] = {
    withSQL {
      select.from(this as a).where.eq(a.email, email).and.eq(a.pass, password)
    }.map {
      Account(a.resultName, _)
    }.single.apply()
  }

}