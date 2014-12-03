package infrastructure

import scala.util.{ Try, Success, Failure }
import play.api.Play.current
import com.typesafe.plugin._

object Mailer {

  // TODO エラー処理！
  def sendMail(subject: String, email: String, contents: String) {
    val mail = use[MailerPlugin].email
    mail.setSubject(subject)
    mail.setRecipient(email)
    mail.setFrom("Layla <miles.davenport@anotheremail.com>")
    mail.send(contents)
  }
}