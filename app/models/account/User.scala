package models.account

import scalikejdbc._

case class User(email: String, name: String, password: String)

object User extends SQLSyntaxSupport[User] {

  override val tableName = "account"
  val a = this.syntax("a")

  def authenticate(email: String, password: String)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(this as a).where.eq(a.email, email).and.eq(a.password, password)
    }.map {
      User(a.resultName, _)
    }.single.apply()
  }

  private def apply(a: ResultName[User], rs: WrappedResultSet): User = User(rs.string(a.email), rs.string(a.name), rs.string(a.password))

}