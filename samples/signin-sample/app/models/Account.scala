package models

import play.api.Play.current
import play.api.cache.Cache
import play.api._
import collection.mutable.Map


case class Account (
		email: String,
		name: Option[String],
		password: String,
		enabled: Boolean
)

object Account {
  
  def findAccountByEmail(email: String): Option[Account] = {
    val accounts = Cache.getAs[Map[String, Account]]("accounts").getOrElse[Map[String, Account]](Map[String, Account]())
    val result = accounts.get(email)
    result match {
      case None => {
	    Logger.info(email + " not found. Something went wrong?")
      }
      case _ => {
	    Logger.info(email + " found")
      }
    }
    result
  }

  def createDisabledAccount(email: String, name: Option[String], password: String): Option[Account]  = {
    val account = Account(email, name, password, false)
    val accounts = Cache.getAs[Map[String, Account]]("accounts").getOrElse[Map[String, Account]](Map[String, Account]())
    accounts.put(email, account)
    Cache.set("accounts", accounts)
    Some(account)
  }

  def enableAccount(email: String): Option[Account]  = {
    val account = findAccountByEmail(email)
    account match {
      case None => {
	    Logger.warn(email + " cannot be enabled. Something went wrong")
        None
      }
      case _ => {
        val modified = account.get.copy(enabled = true)
        val accounts = Cache.getAs[Map[String, Account]]("accounts").getOrElse[Map[String, Account]](Map[String, Account]())
        accounts.put(email, modified)
        Some(modified)
      }
    }
    
  }
  
  
}