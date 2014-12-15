package controllers

import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.Result
import scalikejdbc.DB

trait ControllerSupport extends Controller {
  private type ReqType = Request[AnyContent]
  private type ReqPramMap = Map[String, Seq[String]]

  /** Getパラメータ */
  def reqGetParam(implicit req: ReqType): ReqPramMap = req.queryString

  /** Postパラメータ */
  def reqPostParam(implicit req: ReqType): ReqPramMap =
    req.body.asFormUrlEncoded.getOrElse(Map.empty)

  def execute[M](func1: => Either[Seq[String], M])(func2: M => Result): Result =
    func1.left.map(errors => BadRequest(errors.mkString(","))).right.map(func2).merge

  def executeWithTx[M](func1: => Either[Seq[String], M])(func2: M => Result): Result =
    DB localTx { implicit session => execute(func1)(func2) }

  /** requestのプロトコル */
  def getProtcol(implicit req: ReqType) = if (req.secure) "https" else "http"

  /** requestのドメイン */
  def getDomain(implicit req: ReqType) = req.headers.get("Host").get

  def schemeAndHost(implicit req: ReqType) = s"${getProtcol}://${getDomain}"
}