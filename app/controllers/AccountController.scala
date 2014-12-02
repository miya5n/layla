package controllers

import models.account.Account
import models.account.AccountRepository
import play.api.mvc.Action
import play.api.mvc.Controller

object AccountController extends Controller with AccountRepository {

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

  def entry() = Action { implicit req =>
    Account(req.body.asFormUrlEncoded.getOrElse(Map.empty)).validateForEntry match {
      case Left(a) => BadRequest("だめよー")
      case Right(b) => {
        save(b)
        // メール送信処理
        Ok("") // 登録処理
      }
    }
  }

}