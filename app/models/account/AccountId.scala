package models.account

import infrastructure.Identifier

case class AccountId(id: Long) extends Identifier[Long]