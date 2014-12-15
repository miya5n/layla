package controllers

import models.account.Account
import play.api.mvc.Action

object AccountController extends ControllerSupport {

  /** ログイン */
  def login() = Action { implicit req =>
    execute(Account(reqPostParam) validateForLogin) { acc =>
      Redirect(s"${schemeAndHost}/menu").withSession("email" -> acc.email.get)
    }
  }

  /** 本登録  */
  def regist() = Action { implicit req =>
    executeWithTx(Account(reqGetParam) validateForEntry) { acc =>
      acc.registration
      Ok("本登録完了")
    }
  }

  /** 仮登録  */
  def registAsProvisioning() = Action { implicit req =>
    executeWithTx(Account(reqPostParam) validateForProvisioning) { acc =>
      acc.provisioning(schemeAndHost)
      Ok("仮登録完了")
    }
  }
}