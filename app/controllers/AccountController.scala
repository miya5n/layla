package controllers

import models.account.Account
import models.account.AccountRepository
import play.api.mvc.Action
import play.api.mvc.Controller
import infrastructure.Mailer

object AccountController extends Controller with AccountRepository {

  /** ログイン */
  def login() = Action { implicit req =>
    Account(req.body.asFormUrlEncoded.getOrElse(Map.empty)).validateForLogin match {
      case Left(a) => BadRequest(a.mkString(","))
      case Right(b) => {
        authenticate(b.email.get, b.password.get) match {
          case None          => BadRequest("だめよー")
          case Some(account) => Ok("").withSession("email" -> account.email.get)
        }
      }
    }
  }

  /** 本登録  */
  def registration() = Action { implicit req =>
    Ok("") // 登録処理
  }

  /** 仮登録  */
  def provisionalRegistration() = Action { implicit req =>
    Account(req.body.asFormUrlEncoded.getOrElse(Map.empty)).validateForEntry match {
      case Left(errors) => BadRequest(errors.mkString(","))
      case Right(account) => {
        save(account)
        val requestUrl = s"""${if (req.secure) "https://" else "http://"}${req.headers.get("Host").get}"""
        Mailer.sendMail("仮登録完了メール", account.email.get, mailContents.format(requestUrl)) // メール送信処理
        Ok("") // 登録処理
      }
    }
  }

  val mailContents =
    """この度は本サービスに登録頂き、誠にありがとうございました。
以下のURLにアクセスして本登録を完了させて下さい。

%s/account/registration""" // TODO パラメータがいる
}