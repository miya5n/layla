package controllers

import play.api.mvc.AnyContent
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.Result
import scalikejdbc.DB

trait ControllerSupport extends Controller {
  def reqGetParameter(implicit req: Request[AnyContent]): Map[String, Seq[String]] = req.queryString

  def reqPostParameter(implicit req: Request[AnyContent]): Map[String, Seq[String]] =
    req.body.asFormUrlEncoded.getOrElse(Map.empty)

  def createModel[M](func: => Either[Seq[String], M]): M =
    func.left.map(errors => BadRequest(errors.mkString(","))).right.get

  def execute(func: => Result) = func

  def executeWithTx[M](func: => Result) = DB localTx { implicit session => execute(func) }

  def getProtcol(implicit req: Request[AnyContent]) = if (req.secure) "https" else "http"

  def getDomain(implicit req: Request[AnyContent]) = req.headers.get("Host").get
}