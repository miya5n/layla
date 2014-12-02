package models.support

trait ModelFactory {
  /**
   * TODO まだ修正の余地あり => value(0)はおかしい
   */
  def getOptValue[T](key: String)(implicit data: Map[String, Seq[String]]): Option[T] = {
    data.get(key) match {
      case None => None
      case Some(value) =>
        try {
          Some(value(0).trim().asInstanceOf[T])
        } catch {
          case e: java.lang.ClassCastException => throw new Exception(s"${key} => ClassCastException")
          case _: Throwable                    => throw new Exception(s"${key} => 予期せぬエラー")
        }
    }
  }
}