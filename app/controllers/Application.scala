package controllers

import java.io.File

import play.api.libs.Jsonp
import play.api.libs.json.Json
import play.api.mvc._

object Application extends Controller {

  def index(file: String) = Action {
    var f = new File(file)

    if (f.exists())
      Ok(scala.io.Source.fromFile(f.getCanonicalPath(), "UTF-8").mkString).as("text/html")
    else
      NotFound
  }

  def index2(callback: String) = Action {
    //    Ok(Jsonp(callback, json))
    Ok
  }

  //  val json = Json.parse("""
  //  {
  //    "user": {
  //      "name" : "hoge,
  //      "age" : 25,
  //      "sex" : "male",
  //      "email" : "hoge@example.com",
  //      "job" : "programmer"
  //    }
  //  }""")
}