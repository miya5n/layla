package controllers

import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.Result
import scalikejdbc.DB

trait ControllerSupport extends Controller {
  /** Getパラメータ */
  def reqGetParameter(implicit req: Request[AnyContent]): Map[String, Seq[String]] = req.queryString

  /** Postパラメータ */
  def reqPostParameter(implicit req: Request[AnyContent]): Map[String, Seq[String]] =
    req.body.asFormUrlEncoded.getOrElse(Map.empty)

  def createAndCheck[M](func: => Either[Seq[String], M]) =
    func.left.map(errors => BadRequest(errors.mkString(","))).right

  def execute(func: Either[Result, Result]): Result = func.merge

  def executeWithTx(func: Either[Result, Result]): Result = DB localTx { implicit session => execute(func) }

  /** requestのプロトコル */
  def getProtcol(implicit req: Request[AnyContent]) = if (req.secure) "https" else "http"

  /** requestのドメイン */
  def getDomain(implicit req: Request[AnyContent]) = req.headers.get("Host").get
}