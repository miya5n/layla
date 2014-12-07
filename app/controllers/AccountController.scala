package controllers

import models.account.Account
import play.api.mvc.Action

object AccountController extends ControllerSupport {

  /** ログイン */
  def login() = Action { implicit req =>
    execute {
      val email = createModel { Account(reqPostParameter) validateForLogin }.email.get
      Ok("").withSession("email" -> email)
    }
  }

  /** 本登録  */
  def regist() = Action { implicit req =>
    executeWithTx {
      createModel { Account(reqGetParameter).validateForEntry }.registration
      Ok("本登録完了")
    }
  }

  /** 仮登録  */
  def registAsProvisioning() = Action { implicit req =>
    executeWithTx {
      createModel { Account(reqPostParameter).validateForProvisioning }.provisioning(s"${getProtcol}://${getDomain}")
      Ok("仮登録完了")
    }
  }
}