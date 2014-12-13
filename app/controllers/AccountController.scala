package controllers

import models.account.Account
import play.api.mvc.Action

object AccountController extends ControllerSupport {

  /** ログイン */
  def login() = Action { implicit req =>
    execute {
      createAndCheck(Account(reqPostParameter) validateForLogin).map { acc =>
        Redirect(s"${getProtcol}://${getDomain}/menu").withSession("email" -> acc.email.get)
      }
    }
  }

  /** 本登録  */
  def regist() = Action { implicit req =>
    executeWithTx {
      createAndCheck(Account(reqGetParameter) validateForEntry).map { acc =>
        acc.registration
        Ok("本登録完了")
      }
    }
  }

  /** 仮登録  */
  def registAsProvisioning() = Action { implicit req =>
    executeWithTx {
      createAndCheck(Account(reqPostParameter) validateForProvisioning).map { acc =>
        acc.provisioning(s"${getProtcol}://${getDomain}")
        Ok("仮登録完了")
      }
    }
  }
}