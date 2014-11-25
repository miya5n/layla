package controllers

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.test.WithApplication

@RunWith(classOf[JUnitRunner])
class AccountControllerSpec extends Specification {

  "" should {

    "" in new WithApplication {
      route(FakeRequest(POST, "/account/entry"))
      "" mustEqual ""
    }
  }
}
