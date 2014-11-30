package models.account

import models.infrastructure.Identifier

case class AccountId(id: Long) extends Identifier[Long]