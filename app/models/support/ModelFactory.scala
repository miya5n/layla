package models.support

import scala.collection.mutable.ArrayBuffer

trait ModelFactory {
  /**
   * TODO まだ修正の余地あり => value(0)はおかしい
   */
  def getOptValue[T](key: String)(implicit data: Map[String, Any]): Option[T] = {
    data.get(key) match {
      case None => None
      case Some(any) =>
        try {
          if (any.isInstanceOf[Seq[_]]) {
            val list = any.asInstanceOf[Seq[_]]
            println(list)
            val value = if (list.size == 1) list.head else list
            Some(value.asInstanceOf[T])
          } else {
            Some(any.asInstanceOf[T])
          }
        } catch {
          case e: java.lang.ClassCastException => throw new Exception(s"${key} => ClassCastException")
          case _: Throwable                    => throw new Exception(s"${key} => 予期せぬエラー")
        }
    }
  }
}