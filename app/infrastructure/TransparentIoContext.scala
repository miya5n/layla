package infrastructure

import scalikejdbc.DBSession

case class IoContext[S](session: S)

/**
 * TODO どんな感じで使おうか。。。
 */
trait TransparentIoContext[S] {
  implicit def wrapContext(implicit session: S) = IoContext(session)
}
