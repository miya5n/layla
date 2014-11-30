package support

import org.specs2.mutable.SpecificationWithJUnit
import models.infrastructure.Identifier
import models.account.SexType
import models.infrastructure.Entity
import models.support.Validator
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith

// TODO 作り直し！
@RunWith(classOf[JUnitRunner])
class ValidatorSpec extends SpecificationWithJUnit with Validator {

  "Validator" should {
    "【正常】requireは対象データがある場合、例外は発生しないこと" in {
      val testData = Some("data")
      val result = Check(testData, "データ") is require
      result.item mustEqual testData
    }

    "【異常】requireは対象データが空文字の場合、例外が発生すること" in {
      val testData = Some("")
      val result = try { Check(testData, "データ") is require } catch { case e => e.getMessage() }
      result mustEqual "データを指定してください。"
    }

    "【異常】requireは対象データがNoneの場合、例外が発生すること" in {
      val testData = Option(null)
      val result = try { Check(testData, "データ") is require } catch { case e => e.getMessage() }
      result mustEqual "データを指定してください。"
    }

    "【正常】minLength[String]はデータサイズが指定サイズと同じ場合、例外が発生しないこと" in {
      val testData = Some("abc")
      val result = Check(testData, "データ", min = 3) is minLength
      result.item mustEqual testData
    }

    "【正常】minLength[String]はデータサイズが指定サイズ以上の場合、例外が発生しないこと" in {
      val testData = Some("abcd")
      val result = Check(testData, "データ", min = 3) is minLength
      result.item mustEqual testData
    }

    "【異常】minLength[String]はデータサイズが指定サイズ未満の場合、例外が発生すること" in {
      val testData = Some("ab")
      val result = try { Check(testData, "データ", min = 3) is minLength } catch { case e => e.getMessage() }
      result mustEqual "データは3以上を指定してください。"
    }

    "【正常】minLength[Int]はデータサイズが指定サイズと同じ場合、例外が発生しないこと" in {
      val testData = Some(3)
      val result = Check(testData, "データ", min = 3) is minLength
      result.item mustEqual testData
    }

    "【正常】minLength[Int]はデータサイズが指定サイズ以上の場合、例外が発生しないこと" in {
      val testData = Some(4L)
      val result = Check(testData, "データ", min = 3) is minLength
      result.item mustEqual testData
    }

    "【異常】minLength[Int]はデータサイズが指定サイズ未満の場合、例外が発生すること" in {
      val testData = Some(2)
      val result = try { Check(testData, "データ", min = 3) is minLength } catch { case e => e.getMessage() }
      result mustEqual "データは3以上を指定してください。"
    }

    "【正常】minLength[Long]はデータサイズが指定サイズと同じ場合、例外が発生しないこと" in {
      val testData = Some(3L)
      val result = Check(testData, "データ", min = 3) is minLength
      result.item mustEqual testData
    }

    "【正常】minLength[Long]はデータサイズが指定サイズ以上の場合、例外が発生しないこと" in {
      val testData = Some(4L)
      val result = Check(testData, "データ", min = 3) is minLength
      result.item mustEqual testData
    }

    "【異常】minLength[Long]はデータサイズが指定サイズ未満の場合、例外が発生すること" in {
      val testData = Some(2)
      val result = try { Check(testData, "データ", min = 3) is minLength } catch { case e => e.getMessage() }
      result mustEqual "データは3以上を指定してください。"
    }

    "【正常】maxLength[String]はデータサイズが指定サイズと同じ場合、例外が発生しないこと" in {
      val testData = Some("abc")
      val result = Check(testData, "データ", max = 3) is maxLength
      result.item mustEqual testData
    }

    "【正常】maxLength[String]はデータサイズが指定サイズ以下の場合、例外が発生しないこと" in {
      val testData = Some("ab")
      val result = Check(testData, "データ", max = 3) is maxLength
      result.item mustEqual testData
    }

    "【異常】maxLength[String]はデータサイズが指定サイズより大きい場合、例外が発生すること" in {
      val testData = Some("abcd")
      val result = try { Check(testData, "データ", max = 3) is maxLength } catch { case e => e.getMessage() }
      result mustEqual "データは3以下を指定してください。"
    }

    "【正常】maxLength[Int]はデータサイズが指定サイズと同じ場合、例外が発生しないこと" in {
      val testData = Some(3)
      val result = Check(testData, "データ", max = 3) is maxLength
      result.item mustEqual testData
    }

    "【正常】maxLength[Int]はデータサイズが指定サイズ以下の場合、例外が発生しないこと" in {
      val testData = Some(2)
      val result = Check(testData, "データ", max = 3) is maxLength
      result.item mustEqual testData
    }

    "【異常】maxLength[Int]はデータサイズが指定サイズより大きい場合、例外が発生すること" in {
      val testData = Some(4)
      val result = try { Check(testData, "データ", max = 3) is maxLength } catch { case e => e.getMessage() }
      result mustEqual "データは3以下を指定してください。"
    }

    "【正常】maxLength[Long]はデータサイズが指定サイズと同じ場合、例外が発生しないこと" in {
      val testData = Some(3L)
      val result = Check(testData, "データ", max = 3) is maxLength
      result.item mustEqual testData
    }

    "【正常】maxLength[Long]はデータサイズが指定サイズ以下の場合、例外が発生しないこと" in {
      val testData = Some(2L)
      val result = Check(testData, "データ", max = 3) is maxLength
      result.item mustEqual testData
    }

    "【異常】maxLength[Long]はデータサイズが指定サイズより大きい場合、例外が発生すること" in {
      val testData = Some(4L)
      val result = try { Check(testData, "データ", max = 3) is maxLength } catch { case e => e.getMessage() }
      result mustEqual "データは3以下を指定してください。"
    }

    "【正常】range[Long]はデータサイズが範囲内(最小値)の場合、例外が発生しないこと" in {
      val testData = Some(1L)
      val result = Check(testData, "データ", min = 1, max = 3) is range
      result.item mustEqual testData
    }

    "【正常】range[Long]はデータサイズが範囲内の場合、例外が発生しないこと" in {
      val testData = Some(2L)
      val result = Check(testData, "データ", min = 1, max = 3) is range
      result.item mustEqual testData
    }

    "【正常】range[Long]はデータサイズが範囲内(最大値)の場合、例外が発生しないこと" in {
      val testData = Some(3L)
      val result = Check(testData, "データ", min = 1, max = 3) is range
      result.item mustEqual testData
    }

    "【異常】range[Long]はデータサイズが範囲外(最大値超)の場合、例外が発生すること" in {
      val testData = Some(4L)
      val result = try { Check(testData, "データ", min = 1, max = 3) is range } catch { case e => e.getMessage() }
      result mustEqual "データは3以下を指定してください。"
    }

    "【異常】range[Long]はデータサイズが範囲外(最小値超)の場合、例外が発生すること" in {
      val testData = Some(0L)
      val result = try { Check(testData, "データ", min = 1, max = 3) is range } catch { case e => e.getMessage() }
      result mustEqual "データは1以上を指定してください。"
    }

    "【正常1】Emial[String]はEmail形式の場合、例外が発生しないこと" in {
      val testData = Some("xzzz@gmail.com")
      val result = Check(testData, "Email") is Email
      result.item mustEqual testData
    }

    "【正常2】Emial[String]はEmail形式の場合、例外が発生しないこと" in {
      val testData = Some("x1.zzz@gmail.com")
      val result = Check(testData, "Email") is Email
      result.item mustEqual testData
    }

    "【異常】Emial[String]はEmail形式でない場合(@マークなし)、例外が発生すること" in {
      val testData = Some("xzzzgmail.com")
      val result = try { Check(testData, "Email") is Email } catch { case e => e.getMessage() }
      result mustEqual "Emailの形式が間違っています。"
    }

    "【異常】Emial[String]はEmail形式でない場合(後半ドットなし)、例外が発生すること" in {
      val testData = Some("x1zzz@gmailcom")
      val result = try { Check(testData, "Email") is Email } catch { case e => e.getMessage() }
      result mustEqual "Emailの形式が間違っています。"
    }
  }

}