package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import models.support.EnumLike
import play.api.data.format.Formatter
import models.support.EnumCompanion
import play.api.data.format.Formats._
import models.account.AccountId
import models.account.Account
import models.account.SexType
import models.account.User
import models.account.AccountRepository

object AccountController extends Controller {

  // TODO 早く消したいログインフォーム
  val loginForm = Form(
    tuple(
      "email" -> nonEmptyText,
      "pass" -> nonEmptyText
    ) verifying ("Invalid email or password", result => result match {
        case (email, password) => User.authenticate(email, password).isDefined
      })
  )

  // TODO 早く消したいログインフォーム
  def login() = Action { implicit req =>
    Account(req.body.asFormUrlEncoded.getOrElse(Map.empty)).validateForEntry
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest("だめよー"),
      user => Ok("").withSession("email" -> user._1)
    )
  }

  // TODO leftとrightの使い方がわからない
  def entry() = Action { implicit req =>
    val d = Account(req.body.asFormUrlEncoded.getOrElse(Map.empty)).validateForEntry.left.map(a => BadRequest("")).right.get
    Ok("")
  }
}